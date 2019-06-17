package org.thehellnet.lanparty.manager.model.dto.request.tool;

public class GetCfgToolRequestDTO extends ToolRequestDTO {

    private String barcode;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
