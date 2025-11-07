package br.com.example.vehicleanalysis.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleAnalysisResponse {
    private String vinCanonical;
    private String f1Status;
    private String f2Status;
    private String f3Status;

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private VehicleAnalysisResponse r = new VehicleAnalysisResponse();
        public Builder vinCanonical(String v){ r.vinCanonical = v; return this; }
        public Builder f1Status(String s){ r.f1Status = s; return this; }
        public Builder f2Status(String s){ r.f2Status = s; return this; }
        public Builder f3Status(String s){ r.f3Status = s; return this; }
        public VehicleAnalysisResponse build(){ return r; }
    }

    // getters
    public String getVinCanonical() { return vinCanonical; }
    public String getF1Status() { return f1Status; }
    public String getF2Status() { return f2Status; }
    public String getF3Status() { return f3Status; }
}
