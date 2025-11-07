package br.com.example.vehicleanalysis.infrastructure.persistence;

import br.com.example.vehicleanalysis.application.dto.VehicleAnalysisResponse;
import br.com.example.vehicleanalysis.domain.VehicleAnalysisLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class VehicleAnalysisLogRepositoryImpl implements VehicleAnalysisLogRepository {

    private final SpringDataVehicleAnalysisRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(VehicleAnalysisLogRepositoryImpl.class);

    public VehicleAnalysisLogRepositoryImpl(SpringDataVehicleAnalysisRepository repo) {
        this.repo = repo;
    }

    @Override
    public void saveLog(String id, String timestamp, String idInputValue, String vinCanonical, VehicleAnalysisResponse response) {
        VehicleAnalysisLogEntity e = new VehicleAnalysisLogEntity();
        try {
            e.setSupplierCalls(mapper.writeValueAsString(response));
        } catch (JsonProcessingException ex) {
            e.setSupplierCalls("{}");
        }
        e.setId(id);
        e.setTimestamp(timestamp);
        e.setIdInputValue(idInputValue);
        e.setVinCanonical(vinCanonical);
        e.setHasConstraints("FAILED".equals(response.getF1Status()) ? false : true);
        repo.save(e);
        logger.info("Saved vehicle analysis log {}", e.getId());
    }
}
