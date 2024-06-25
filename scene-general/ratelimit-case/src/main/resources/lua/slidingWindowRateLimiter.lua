-- 基于滑动窗口限流 Lua 脚本
-- 限流对象
local key = KEYS[1]
-- 窗口大小
local windowSize = tonumber(ARGV[1])
-- 阈值
local limit = tonumber(ARGV[2])

local currentTimestamp = tonumber(redis.call('TIME')[1])
local windowStartTimestamp = currentTimestamp - windowSize

-- 移除已过期的
redis.call('ZREMRANGEBYSCORE', key, '-inf', windowStartTimestamp)

-- 获取窗口内请求数量
local count = redis.call('ZCARD', key)

-- 判断是否超过限流阈值
if count > limit then
    -- 1 限流
    return 1
else
    -- 添加当前时间戳
    redis.call('ZADD', key, currentTimestamp, currentTimestamp)
    -- 0 不限流
    return 0
end
