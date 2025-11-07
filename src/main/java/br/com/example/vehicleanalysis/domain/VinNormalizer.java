package br.com.example.vehicleanalysis.domain;

import org.springframework.stereotype.Component;

@Component
public class VinNormalizer {

    public String normalizeToVin(String input) {
        // naive normalization: if looks like VIN length 17 -> return, else try map (stub)
        if (input == null) return null;
        String s = input.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        if (s.length() == 17) return s;
        // TODO: implement plate/renavam -> VIN lookup (cache or external service)
        // For skeleton return padded value (for tests replace with real mapping)
        return String.format("VIN_%s", s);
    }
}
