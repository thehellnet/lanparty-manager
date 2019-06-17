package org.thehellnet.lanparty.manager.model.dto.response.tool;

import java.util.List;

public class GetCfgToolResponseDTO extends ToolResponseDTO {

    private List<String> cfg;

    public GetCfgToolResponseDTO() {
    }

    public GetCfgToolResponseDTO(List<String> cfg) {
        this.cfg = cfg;
    }

    public List<String> getCfg() {
        return cfg;
    }

    public void setCfg(List<String> cfg) {
        this.cfg = cfg;
    }
}
