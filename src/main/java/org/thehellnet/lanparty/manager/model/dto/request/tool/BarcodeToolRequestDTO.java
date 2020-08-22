package org.thehellnet.lanparty.manager.model.dto.request.tool;

public class BarcodeToolRequestDTO extends ToolRequestDTO {

    protected String barcode;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
