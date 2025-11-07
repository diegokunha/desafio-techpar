package br.com.example.vehicleanalysis.api;

import br.com.example.vehicleanalysis.application.VehicleAnalysisService;
import br.com.example.vehicleanalysis.application.dto.VehicleAnalysisResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/veiculos")
public class VehicleAnalysisController {

    private final VehicleAnalysisService service;

    public VehicleAnalysisController(VehicleAnalysisService service) {
        this.service = service;
    }

    @GetMapping("/{idInput}/analise")
    public ResponseEntity<VehicleAnalysisResponse> analyze(
            @PathVariable("idInput") String idInput,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {

        VehicleAnalysisResponse response = service.analyze(idInput, idempotencyKey);
        return ResponseEntity.ok(response);
    }
}
