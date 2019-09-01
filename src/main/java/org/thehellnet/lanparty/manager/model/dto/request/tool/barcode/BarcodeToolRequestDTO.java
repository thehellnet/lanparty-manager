package org.thehellnet.lanparty.manager.model.dto.request.tool.barcode;

import org.thehellnet.lanparty.manager.model.dto.request.tool.ToolRequestDTO;

public class BarcodeToolRequestDTO extends ToolRequestDTO {

    private String barcode;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
