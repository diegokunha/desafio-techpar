package br.com.example.vehicleanalysis.infrastructure.persistence;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "vehicle_analysis_log")
public class VehicleAnalysisLogEntity {

    @Id
    private String id;

    private String timestamp;
    private String idInputValue;
    private String vinCanonical;

    @Column(columnDefinition = "text")
    private String supplierCalls;

    private boolean hasConstraints;
    private long estimatedCostCents;
    private String traceId;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID().toString();
    }

    // getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getIdInputValue() { return idInputValue; }
    public void setIdInputValue(String idInputValue) { this.idInputValue = idInputValue; }
    public String getVinCanonical() { return vinCanonical; }
    public void setVinCanonical(String vinCanonical) { this.vinCanonical = vinCanonical; }
    public String getSupplierCalls() { return supplierCalls; }
    public void setSupplierCalls(String supplierCalls) { this.supplierCalls = supplierCalls; }
    public boolean isHasConstraints() { return hasConstraints; }
    public void setHasConstraints(boolean hasConstraints) { this.hasConstraints = hasConstraints; }
    public long getEstimatedCostCents() { return estimatedCostCents; }
    public void setEstimatedCostCents(long estimatedCostCents) { this.estimatedCostCents = estimatedCostCents; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
}
