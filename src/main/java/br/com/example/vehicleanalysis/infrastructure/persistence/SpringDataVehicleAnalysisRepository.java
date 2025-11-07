package br.com.example.vehicleanalysis.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataVehicleAnalysisRepository extends JpaRepository<VehicleAnalysisLogEntity, String> {
}
