package br.com.example.vehicleanalysis.domain;

public interface VinProviderClient {
    /**
     * Try to resolve an input (plate/renavam) to VIN via external provider.
     * Returns null if not found or on error.
     */
    String resolve(String input);
}
