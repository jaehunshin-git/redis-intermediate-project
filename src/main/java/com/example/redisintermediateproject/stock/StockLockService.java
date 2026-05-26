package com.example.redisintermediateproject.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class StockLockService {
    private final RedisTemplate<String, String> redisTemplate;
    private final StockService stockService;

    public void decrease(Long id) throws InterruptedException {
        String key = "stock_lock" + id;

        while (!tryLock(key)) {
            Thread.sleep(100);

        }

        try {
            stockService.decrease(id);

        } finally {
            redisTemplate.delete(key);
        }

    }

    private boolean tryLock(String key) {
        // SET [key] [value] NX EX [seconds]
        return redisTemplate
                .opsForValue()
                .setIfAbsent(key, "lock", Duration.ofMillis(3_000));
    }
}

