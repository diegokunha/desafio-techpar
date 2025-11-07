package br.com.example.vehicleanalysis.infrastructure.client;

import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class F3Client {

    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(F3Client.class);

    public static class F3Response {
        public boolean ok = true;
    }

    public F3Client(WebClient webClient) {
        this.webClient = webClient;
    }

    @Retry(name = "f3", fallbackMethod = "fallback")
    public F3Response fetch(String vin) {
        try {
            Mono<F3Response> mono = webClient.get()
                    .uri("http://localhost:8089/f3/vehicles/{vin}", vin)
                    .retrieve()
                    .bodyToMono(F3Response.class)
                    .timeout(Duration.ofSeconds(2));
            return mono.block();
        } catch (Exception e) {
            logger.warn("F3 fetch failed: {}", e.getMessage());
            return fallback(vin, e);
        }
    }

    public F3Response fallback(String vin, Throwable t) {
        F3Response r = new F3Response();
        r.ok = false;
        return r;
    }
}
