package br.com.example.vehicleanalysis.infrastructure.client.jaxb;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "GetF1Response")
public class GetF1Response {
    private boolean renajud;
    private boolean recall;

    @XmlElement public boolean isRenajud() { return renajud; }
    public void setRenajud(boolean renajud) { this.renajud = renajud; }
    @XmlElement public boolean isRecall() { return recall; }
    public void setRecall(boolean recall) { this.recall = recall; }
}
