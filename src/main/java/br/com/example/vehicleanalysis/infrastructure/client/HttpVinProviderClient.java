package br.com.example.vehicleanalysis.infrastructure.client;

import br.com.example.vehicleanalysis.domain.VinProviderClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class HttpVinProviderClient implements VinProviderClient {

    private final WebClient webClient;
    private final String baseUrl;
    private static final Logger logger = LoggerFactory.getLogger(HttpVinProviderClient.class);

    public HttpVinProviderClient(WebClient webClient, @Value("${vin.provider.url:}") String baseUrl) {
        this.webClient = webClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public String resolve(String input) {
        if (baseUrl == null || baseUrl.isBlank()) {
            logger.debug("No VIN provider configured (vin.provider.url empty) - skipping external lookup");
            return null;
        }
        try {
            Mono<VinResponse> mono = webClient.get()
                    .uri(baseUrl + "/v1/lookup?query={q}", input)
                    .retrieve()
                    .bodyToMono(VinResponse.class)
                    .timeout(Duration.ofSeconds(3));
            VinResponse r = mono.block();
            if (r != null && r.vin != null && !r.vin.isBlank()) return r.vin;
        } catch (Exception e) {
            logger.warn("Vin provider lookup failed: {}", e.getMessage());
        }
        return null;
    }

    public static class VinResponse { public String vin; }
}
