package org.thehellnet.lanparty.manager.model.dto.service;

public class GameGametypeServiceDTO extends ServiceDTO {

    public Long gameId;
    public Long gametypeId;
    public String tag;

    public GameGametypeServiceDTO() {
    }

    public GameGametypeServiceDTO(Long gameId, Long gametypeId, String tag) {
        this.gameId = gameId;
        this.gametypeId = gametypeId;
        this.tag = tag;
    }
}
