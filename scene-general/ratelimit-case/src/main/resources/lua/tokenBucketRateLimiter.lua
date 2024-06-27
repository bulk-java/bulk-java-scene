-- 基于令牌桶实现，参考 Redisson  RRateLimiter 实现 org.redisson.RedissonRateLimiter.tryAcquireAsync(org.redisson.client.protocol.RedisCommand<T>, java.lang.Long)
-- 本质上和滑动窗口类似，都是通过有序集合zset来保存缓存信息，时间戳作为score，通过时间戳维护时间窗口或令牌的有效性
-- 区别在于滑动窗口是保存每次请求的时间搓来计分，而令牌桶保存的是许可证生成的时间戳。
--参数说明:
--KEYS[1]: 限流器 key
--KEYS[2]: 桶中剩余的许可证  key
--KEYS[3]: 记录所有许可证发出的时间戳的   key

--ARGV[1]: 本次请求申请的令牌数
--ARGV[2]: 请求时间搓
--ARGV[3]: 随机数
--ARGV[4]: 令牌桶容量
--ARGV[5]: 时间周期

-- 查询 key 存储的限流信息
local limitInfo = redis.call('hmget', KEYS[1], 'rate', 'interval');
local rate = limitInfo[1];
local interval = limitInfo[2];
-- 如果差不多当前key的信息，则初始化限流信息
if rate == false then
    -- rate表示令牌桶的最大容量
    rate = tonumber(ARGV[4]);
    -- 生成令牌的时间周期
    interval = tonumber(ARGV[5]);
    redis.call('hmset', KEYS[1], 'rate', rate, 'interval', interval);
end ;

-- 限流器名称
local valueName = KEYS[2];
local permitsName = KEYS[3];
-- 单机时才需要
-- if type == '1' then
--    valueName = KEYS[3];
--    permitsName = KEYS[5];
-- end;
assert(tonumber(rate) >= tonumber(ARGV[1]), 'Requested permits amount could not exceed defined rate');

-- 获取限流器中的剩余许可数量
local currentValue = redis.call('get', valueName);
local res;
if currentValue ~= false then
    -- 如果有令牌过期，就会在下次请求时，重新发放令牌，重新发放的令牌数量 released
    local expiredValues = redis.call('zrangebyscore', permitsName, 0, tonumber(ARGV[2]) - interval);
    local released = 0;
    for i, v in ipairs(expiredValues) do
        -- permits表示单次请求发放的令牌个数
        local random, permits = struct.unpack('Bc0I', v);
        released = released + permits;
    end ;

    -- 更新剩余许可数量currentValue
    if released > 0 then
        --  清除过期的许可
        redis.call('zremrangebyscore', permitsName, 0, tonumber(ARGV[2]) - interval);
        -- 如果  剩余许可数   +  发放的许可数   >  桶的大小tonumber(rate)，桶已经满了
        if tonumber(currentValue) + released > tonumber(rate) then
            --   剩余许可  = 请求许可数  -     已发放的未过期的许可数
            currentValue = tonumber(rate) - redis.call('zcard', permitsName);
        else
            -- 桶未满，发放新的许可数量，更新剩余可用许可：剩余许可  = 当前剩余许可 + 新发放的许可
            currentValue = tonumber(currentValue) + released;
        end ;
        redis.call('set', valueName, currentValue);
    end ;

    --  如果剩余许可不够，需要在res中返回下个许可需要等待多长时间
    if tonumber(currentValue) < tonumber(ARGV[1]) then
        -- zrange获取有序集合下标区间 0 至 0 的成员，firstValue表示第一个许可生成的时间
        local firstValue = redis.call('zrange', permitsName, 0, 0, 'withscores');
        -- 计算下次许可生成的时间
        -- res = 3 + interval - (tonumber(ARGV[2]) - tonumber(firstValue[2]));
        res = 0;
    else
        -- 保存发放的许可：生成许可保存到permitsName中,
        redis.call('zadd', permitsName, ARGV[2], struct.pack('Bc0I', string.len(ARGV[3]), ARGV[3], ARGV[1]));
        -- 减去valueName中的许可数量
        redis.call('decrby', valueName, ARGV[1]);
        res = 1;
    end ;
else
    -- 初始化限流器，struct.pack用于数据打包
    redis.call('set', valueName, rate);
    -- ARGV[2] 表示分数，即时间戳,   ARGV[1]为申请的令牌数
    redis.call('zadd', permitsName, ARGV[2], struct.pack('Bc0I', string.len(ARGV[3]), ARGV[3], ARGV[1]));
    redis.call('decrby', valueName, ARGV[1]);
    res = 1;
end ;

-- 刷新过期时间
-- Pttl命令以毫秒为单位返回 key 的剩余过期时间 ，如果存在过期时间，需要更新valueName和permitsName对象的过期时间
local ttl = redis.call('pttl', KEYS[1]);
if ttl > 0 then
    redis.call('pexpire', valueName, ttl);
    redis.call('pexpire', permitsName, ttl);
end ;
return res;
