package com.example.redisintermediateproject.dau;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DailyActiveUserService {

  private final DailyActiveUserRepository dailyActiveUserRepository;
  private final RedisTemplate<String, String> redisTemplate;

  @Transactional
  public void recordActiveUser(Long userId) {
    LocalDate today = LocalDate.now();
    // 이미 활동 기록이 있는 유저라면 저장하지 않는다.
    if (!dailyActiveUserRepository.existsByUserIdAndActiveDate(userId, today)) {
      // 활동 기록 저장
      dailyActiveUserRepository.save(new DailyActiveUser(userId, today));
    }
  }

  @Transactional(readOnly = true)
  public long getDau(LocalDate date) {
    // 특정 날짜의 DAU를 SQL문의 COUNT를 활용해 계산
    return dailyActiveUserRepository.countByActiveDate(date);
  }

  public void recordActiveUserWithRedis(Long userId) {
    LocalDate today = LocalDate.now();
    String key = "dau:" + today.toString();

    // SADD [key] [value]
//    redisTemplate.opsForSet().add(key, userId.toString());

    // SETBIT [key] [offset] [value]
    redisTemplate.opsForValue().setBit(key, userId, true);
  }

  public Long getDauWithRedis(LocalDate date) {
    String key = "dau:" + date.toString();

    // SCARD [key]
//    return redisTemplate.opsForSet().size(key);

    // BITCOUNT [key]
    return redisTemplate.execute(
            (RedisCallback<Long>) (connection) -> connection.bitCount(key.getBytes())
    );
  }
}
