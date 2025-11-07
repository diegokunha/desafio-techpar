package br.com.example.vehicleanalysis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class WebServiceConfig {

    @Bean
    public WebServiceTemplate webServiceTemplate() {
        WebServiceTemplate wst = new WebServiceTemplate();
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // In real project setContextPath to generated JAXB package for WSDL
        marshaller.setPackagesToScan("br.com.example.vehicleanalysis.infrastructure.client.jaxb");
        wst.setMarshaller(marshaller);
        wst.setUnmarshaller(marshaller);
        return wst;
    }
}
