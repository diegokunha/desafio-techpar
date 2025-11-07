package br.com.example.vehicleanalysis.infrastructure.idempotency;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class IdempotencyFilter implements Filter {

    private final IdempotencyRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(IdempotencyFilter.class);

    public IdempotencyFilter(IdempotencyRepository repo) {
        this.repo = repo;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, jakarta.servlet.ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String key = req.getHeader("Idempotency-Key");
        if (key == null || key.isBlank()) {
            chain.doFilter(request, response);
            return;
        }

        String cached = repo.get(key);
        if (cached != null && !"PROCESSING".equals(cached)) {
            logger.info("Replaying cached response for idempotency key {}", key);
            // cached contains JSON with status|body
            int sep = cached.indexOf('|');
            if (sep > 0) {
                String status = cached.substring(0, sep);
                String body = cached.substring(sep + 1);
                res.setStatus(Integer.parseInt(status));
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write(body);
                return;
            }
        } else if (cached != null && "PROCESSING".equals(cached)) {
            logger.info("Request with idempotency key {} is already processing", key);
            res.setStatus(409);
            res.getWriter().write("{\"error\":\"processing\"}");
            return;
        }

        // Acquire processing marker
        repo.save(key, "PROCESSING", 60);

        CachedHttpServletResponse cachingResponse = new CachedHttpServletResponse(res);
        try {
            chain.doFilter(request, cachingResponse);
            int status = cachingResponse.getStatus();
            String body = new String(cachingResponse.getBody(), StandardCharsets.UTF_8);
            // store status|body with TTL (e.g., 24h)
            repo.save(key, status + "|" + body, 24 * 3600);
            // write captured body to real response
            res.setStatus(status);
            res.setContentType(cachingResponse.getContentType());
            res.getOutputStream().write(cachingResponse.getBody());
        } finally {
            // no-op
        }
    }
}
