package br.com.example.vehicleanalysis.infrastructure.idempotency;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class RedisIdempotencyRepository implements IdempotencyRepository {

    private final StringRedisTemplate redis;

    public RedisIdempotencyRepository(StringRedisTemplate redis) {
        this.redis = redis;
    }

    @Override
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redis.hasKey(key));
    }

    @Override
    public void save(String key, String payload, long ttlSeconds) {
        redis.opsForValue().set(key, payload, Duration.ofSeconds(ttlSeconds));
    }

    @Override
    public String get(String key) {
        return redis.opsForValue().get(key);
    }
}
