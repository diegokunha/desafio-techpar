package br.com.example.vehicleanalysis.domain;

import br.com.example.vehicleanalysis.application.dto.VehicleAnalysisResponse;

public interface VehicleAnalysisLogRepository {
    void saveLog(String id, String timestamp, String idInputValue, String vinCanonical, VehicleAnalysisResponse response);
}
