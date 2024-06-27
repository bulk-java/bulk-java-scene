-- 基于漏桶算法实现限流
--KEYS[1]: 限流器的 key
--ARGV[1]: 容量，决定最大的并发量
--ARGV[2]: 漏水速率，决定平均的并发量
--ARGV[3]: 一次请求的加水量
--ARGV[4]: 时间戳
local limitInfo = redis.call('hmget', KEYS[1], 'capacity', 'passRate', 'addWater', 'water', 'lastTs')
local capacity = limitInfo[1]
local passRate = limitInfo[2]
local addWater = tonumber(limitInfo[3])
local water = limitInfo[4]
local lastTs = limitInfo[5]

local allowed = 1
-- 初始化漏斗
if capacity == false then
    capacity = tonumber(ARGV[1])
    passRate = tonumber(ARGV[2])
    --请求一次所要加的水量,一定不能大于容量值的
    addWater = tonumber(ARGV[3])
    -- 当前储水量，初始水位一般为0
    water = tonumber(0)
    lastTs = tonumber(ARGV[4])
    redis.call('hmset', KEYS[1], 'capacity', capacity, 'passRate', passRate, 'addWater', addWater, 'water', water, 'lastTs', lastTs)
    -- 刚初始化说明第一个访问，直接通过
    allowed = 1
else
    local nowTs = tonumber(ARGV[4])
    -- 计算距离上一次请求到现在的漏水量 =  流水速度 *  (nowTs - lastTs)
    local waterPass = tonumber(passRate * (nowTs - lastTs))
    -- 计算当前剩余水量   =  上次水量  - 时间间隔中流失的水量
    water = math.max(tonumber(0), tonumber(water - waterPass))
    -- 判断是否可以加水   （容量 - 当前水量 >= 增加水量，判断剩余容量是否能够容纳增加的水量）
    if tonumber(capacity - water) >= addWater then
        -- 加水
        water = water + addWater
        -- 更新增加后的当前水量和时间戳
        redis.call('hmset', KEYS[1], 'water', water, 'lastTs', nowTs)
        allowed = 1
    end
    -- 请求失败
    allowed = 0
end

return allowed