package br.com.example.vehicleanalysis.application.mappers;

import br.com.example.vehicleanalysis.application.dto.VehicleAnalysisResponse;
import br.com.example.vehicleanalysis.infrastructure.client.F1Client;
import br.com.example.vehicleanalysis.infrastructure.client.F2Client;
import br.com.example.vehicleanalysis.infrastructure.client.F3Client;

public class SupplierMappers {

    public static VehicleAnalysisResponse map(String vin, F1Client.F1Response f1, F2Client.F2Response f2, F3Client.F3Response f3) {
        return VehicleAnalysisResponse.builder()
                .vinCanonical(vin)
                .f1Status(f1 != null ? (f1.source != null ? f1.source : "OK") : "FAILED")
                .f2Status(f2 != null ? (f2.ok ? "OK" : "FAILED") : "SKIPPED")
                .f3Status(f3 != null ? (f3.ok ? "OK" : "FAILED") : "FAILED")
                .build();
    }
}
