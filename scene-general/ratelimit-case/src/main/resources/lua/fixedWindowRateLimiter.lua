-- 固定窗口限流
-- 限流 key
local key = KEYS[1];
-- 阈值
local max = tonumber(ARGV[1]);
-- 时间窗口，计数器的过期时间
local timeout = tonumber(ARGV[2]);

local allowed = 1;
-- 每次调用，计数器rateLimitKey的值都会加1
local currValue = redis.call('incr', key);

if (currValue == 1) then
    --  初次调用时，通过给计数器key设置过期时间timeout达到固定时间窗口的目的
    --redis.call('expire', key, timeout);
    -- pexpire 单位是毫秒 ，expire 单位是秒,要和自己的入参对应上才行
    redis.call('pexpire', key, timeout);
    allowed = 1;
else
    --  当计数器的值（固定时间窗口内） 大于max时，返回0，不允许访问
    if (currValue > max) then
        allowed = 0;
    end
end
return allowed
