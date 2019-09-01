package org.thehellnet.lanparty.manager.model.dto.request.tool.barcode;

import java.util.List;

public class SaveCfgToolRequestDTO extends BarcodeToolRequestDTO {

    private List<String> cfgLines;

    public List<String> getCfgLines() {
        return cfgLines;
    }

    public void setCfgLines(List<String> cfgLines) {
        this.cfgLines = cfgLines;
    }
}
