package br.com.example.vehicleanalysis.infrastructure.client;

import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class F2Client {

    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(F2Client.class);

    public static class F2Response {
        public boolean ok = true;
    }

    public F2Client(WebClient webClient) {
        this.webClient = webClient;
    }

    @Retry(name = "f2", fallbackMethod = "fallback")
    public F2Response fetch(String vin) {
        try {
            Mono<F2Response> mono = webClient.get()
                    .uri("http://localhost:8089/f2/vehicles/{vin}", vin)
                    .retrieve()
                    .bodyToMono(F2Response.class)
                    .timeout(Duration.ofSeconds(2));
            return mono.block();
        } catch (Exception e) {
            logger.warn("F2 fetch failed: {}", e.getMessage());
            return fallback(vin, e);
        }
    }

    public F2Response fallback(String vin, Throwable t) {
        F2Response r = new F2Response();
        r.ok = false;
        return r;
    }
}
