package org.thehellnet.lanparty.manager.model.dto.service;

public class CfgServiceDTO extends ServiceDTO {

    public Long playerId;
    public Long gameId;
    public String cfgContent;

    public CfgServiceDTO() {
    }

    public CfgServiceDTO(Long playerId, Long gameId, String cfgContent) {
        this.playerId = playerId;
        this.gameId = gameId;
        this.cfgContent = cfgContent;
    }
}
