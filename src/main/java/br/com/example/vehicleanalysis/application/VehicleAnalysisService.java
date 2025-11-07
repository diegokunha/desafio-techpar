package br.com.example.vehicleanalysis.application;

import br.com.example.vehicleanalysis.application.dto.VehicleAnalysisResponse;
import org.springframework.stereotype.Service;

@Service
public class VehicleAnalysisService {

    private final Orchestrator orchestrator;

    public VehicleAnalysisService(Orchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    public VehicleAnalysisResponse analyze(String idInput, String idempotencyKey) {
        return orchestrator.analyze(idInput, idempotencyKey);
    }
}
