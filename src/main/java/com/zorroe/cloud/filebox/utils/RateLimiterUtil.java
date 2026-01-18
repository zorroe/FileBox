package com.zorroe.cloud.filebox.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimiterUtil {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 尝试获取令牌（滑动窗口计数）
     *
     * @param key           限流键（如 ip:192.168.1.1 或 code:ABCD1234）
     * @param maxCount      最大允许次数
     * @param windowSeconds 时间窗口（秒）
     * @return true 表示允许，false 表示被限流
     */
    public boolean tryAcquire(String key, int maxCount, int windowSeconds) {
        String redisKey = "rate_limit:" + key;
        Long current = redisTemplate.opsForValue().increment(redisKey);

        if (current == 1) {
            // 第一次访问，设置过期时间
            redisTemplate.expire(redisKey, windowSeconds, TimeUnit.SECONDS);
        }

        return current <= maxCount;
    }
}
