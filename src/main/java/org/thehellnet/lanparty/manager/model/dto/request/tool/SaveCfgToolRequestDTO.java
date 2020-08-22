package org.thehellnet.lanparty.manager.model.dto.request.tool;

import java.util.List;

public class SaveCfgToolRequestDTO extends BarcodeToolRequestDTO {

    protected List<String> cfgLines;

    public List<String> getCfgLines() {
        return cfgLines;
    }

    public void setCfgLines(List<String> cfgLines) {
        this.cfgLines = cfgLines;
    }
}
