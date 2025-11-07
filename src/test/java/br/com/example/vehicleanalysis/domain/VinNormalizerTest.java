package br.com.example.vehicleanalysis.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VinNormalizerTest {

    @Test
    void normalize_vin_if_17_chars() {
        VinNormalizer n = new VinNormalizer();
        String vin = n.normalizeToVin("1HGCM82633A004352");
        assertEquals("1HGCM82633A004352", vin);
    }

    @Test
    void normalize_plate_to_vin_stub() {
        VinNormalizer n = new VinNormalizer();
        String vin = n.normalizeToVin("ABC-1234");
        assertTrue(vin.startsWith("VIN_"));
    }
}
