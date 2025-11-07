package br.com.example.vehicleanalysis.integration;

import br.com.example.vehicleanalysis.infrastructure.client.F1Client;
import br.com.example.vehicleanalysis.infrastructure.client.F2Client;
import br.com.example.vehicleanalysis.infrastructure.client.F3Client;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.test.client.MockWebServiceServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ExternalClientsIntegrationTest {

    @Autowired
    private WebServiceTemplate webServiceTemplate;

    @Autowired
    private F1Client f1;

    @Autowired
    private F2Client f2;

    @Autowired
    private F3Client f3;

    private MockWebServiceServer mockF1;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() {
        mockF1 = MockWebServiceServer.createServer(webServiceTemplate);
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
        mockF1.reset();
    }

    @Test
    void f2_and_f3_wiremock_and_f1_mocked() {
        // WireMock stubs for F2 and F3
        wireMockServer.stubFor(get(urlPathMatching("/f2/vehicles/.*")).willReturn(aResponse().withStatus(200).withBody("{\"ok\":true}")));
        wireMockServer.stubFor(get(urlPathMatching("/f3/vehicles/.*")).willReturn(aResponse().withStatus(200).withBody("{\"ok\":true}")));

        // For F1 we won't invoke the real SOAP but we can assert that fetch returns stubbed default
        F1Client.F1Response resp = f1.fetch("VIN_ABC123");
        assertThat(resp).isNotNull();
        assertThat(resp.source).isEqualTo("stub");

        F2Client.F2Response r2 = f2.fetch("VIN_ABC123");
        assertThat(r2).isNotNull();
        assertThat(r2.ok).isTrue();

        F3Client.F3Response r3 = f3.fetch("VIN_ABC123");
        assertThat(r3).isNotNull();
        assertThat(r3.ok).isTrue();
    }
}
