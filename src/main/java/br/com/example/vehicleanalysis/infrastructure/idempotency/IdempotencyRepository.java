package br.com.example.vehicleanalysis.infrastructure.idempotency;

public interface IdempotencyRepository {
    boolean exists(String key);
    void save(String key, String payload, long ttlSeconds);
    String get(String key);
}
