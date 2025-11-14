package br.com.example.vehicleanalysis.infrastructure.client;

import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

@Component
public class F1Client {

    private final WebServiceTemplate wsTemplate;
    private static final Logger logger = LoggerFactory.getLogger(F1Client.class);

    public static class Constraints {
        public boolean renajud;
        public boolean recall;
    }

    public static class F1Response {
        public Constraints restricoes = new Constraints();
        public String source;
    }

    public F1Client(WebServiceTemplate wsTemplate) {
        this.wsTemplate = wsTemplate;
    }

    @Retry(name = "f1", fallbackMethod = "fallback")
    public F1Response fetch(String vin) {
        F1Response r = new F1Response();
        r.restricoes.renajud = false;
        r.restricoes.recall = false;
        r.source = "stub";
        return r;
    }

    public F1Response fallback(String vin, Throwable t) {
        F1Response r = new F1Response();
        r.restricoes.renajud = false;
        r.restricoes.recall = false;
        r.source = "fallback";
        return r;
    }
}
