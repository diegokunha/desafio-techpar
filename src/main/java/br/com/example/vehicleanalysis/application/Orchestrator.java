package br.com.example.vehicleanalysis.application;

import br.com.example.vehicleanalysis.application.dto.VehicleAnalysisResponse;
import br.com.example.vehicleanalysis.application.mappers.SupplierMappers;
import br.com.example.vehicleanalysis.domain.VehicleAnalysisLogRepository;
import br.com.example.vehicleanalysis.domain.VinLookupService;
import br.com.example.vehicleanalysis.infrastructure.client.F1Client;
import br.com.example.vehicleanalysis.infrastructure.client.F2Client;
import br.com.example.vehicleanalysis.infrastructure.client.F3Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class Orchestrator {

    private final VinLookupService vinLookup;
    private final F1Client f1;
    private final F2Client f2;
    private final F3Client f3;
    private final VehicleAnalysisLogRepository repo;

    private static final Logger logger = LoggerFactory.getLogger(Orchestrator.class);

    public Orchestrator(VinLookupService vinLookup,
                        F1Client f1,
                        F2Client f2,
                        F3Client f3,
                        VehicleAnalysisLogRepository repo) {
        this.vinLookup = vinLookup;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.repo = repo;
    }

    public VehicleAnalysisResponse analyze(String idInput, String idempotencyKey) {
        // Generate a unique identifier for tracing and add it to the MDC (Mapped Diagnostic Context) for logging.
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);

        // Resolve the VIN (Vehicle Identification Number) from the provided idInput.
        String vin = vinLookup.resolveToVin(idInput);

        // Record the start time to calculate the total execution time of the process.
        long start = System.currentTimeMillis();

        // Start an asynchronous call to fetch information from the F1 client.
        CompletableFuture<F1Client.F1Response> f1Future = CompletableFuture.supplyAsync(() -> {
            try {
                return f1.fetch(vin);
            } catch (Exception e) {
                logger.warn("f1 failed: {}", e.getMessage());
                return null;
            }
        });

        // Start an asynchronous call to fetch information from the F3 client.
        CompletableFuture<F3Client.F3Response> f3Future = CompletableFuture.supplyAsync(() -> {
            try {
                return f3.fetch(vin);
            } catch (Exception e) {
                logger.warn("f3 failed: {}", e.getMessage());
                return null;
            }
        });

        // Declare variables to store the responses from the F1 and F3 clients.
        F1Client.F1Response f1Resp = null;
        F3Client.F3Response f3Resp = null;

        // Attempt to retrieve the response from the F1 client with a timeout of 5000ms.
        try {
            f1Resp = f1Future.get(5000, null);
        } catch (Exception e) {
            logger.warn("F1 request timed out or failed: {}", e.getMessage());
        }

        // Attempt to retrieve the response from the F3 client with a timeout of 5000ms.
        try {
            f3Resp = f3Future.get(5000, null);
        } catch (Exception e) {
            logger.warn("F3 request timed out or failed: {}", e.getMessage());
        }

        // Check if it is necessary to query the F2 client based on the restrictions returned by the F1 client.
        boolean needsF2 = f1Resp != null && (f1Resp.restricoes != null && (f1Resp.restricoes.renajud || f1Resp.restricoes.recall));
        F2Client.F2Response f2Resp = null;

        // If necessary, make the call to the F2 client to fetch additional information.
        if (needsF2) {
            try {
                f2Resp = f2.fetch(vin);
            } catch (Exception e) {
                logger.warn("F2 failed: {}", e.getMessage());
            }
        }

        // Map the responses from the clients and the VIN to a consolidated response object.
        VehicleAnalysisResponse response = SupplierMappers.map(vin, f1Resp, f2Resp, f3Resp);

        // Persist a log of the analysis in the repository, including information such as VIN, input, and response.
        repo.saveLog(UUID.randomUUID().toString(), Instant.now().toString(), idInput, vin, response);

        // Calculate the total execution time of the process.
        long elapsed = System.currentTimeMillis() - start;
        logger.info("Analyze completed in {} ms", elapsed);

        // Remove the traceId from the MDC to avoid context leakage.
        MDC.remove("traceId");

        // Return the consolidated analysis response.
        return response;
    }
}
