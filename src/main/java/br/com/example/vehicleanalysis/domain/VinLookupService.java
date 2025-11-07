package br.com.example.vehicleanalysis.domain;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class VinLookupService {

    private final Cache<String, String> cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofHours(6))
            .maximumSize(10_000)
            .build();

    private final VinProviderClient vinProviderClient;

    public VinLookupService(VinProviderClient vinProviderClient) {
        this.vinProviderClient = vinProviderClient;
    }

    /**
     * Try to resolve a VIN from various input types (VIN, plate, renavam).
     * First consults local cache, then an external provider if configured.
     */
    public String resolveToVin(String input) {
        if (input == null) return null;
        String normalized = input.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        String cached = cache.getIfPresent(normalized);
        if (cached != null) return cached;
        if (normalized.length() == 17) {
            cache.put(normalized, normalized);
            return normalized;
        }
        // Try external provider
        String external = vinProviderClient.resolve(normalized);
        if (external != null && !external.isBlank()) {
            cache.put(normalized, external);
            return external;
        }
        // fallback stub mapping
        String synthetic = "VIN_" + normalized;
        cache.put(normalized, synthetic);
        return synthetic;
    }
}
