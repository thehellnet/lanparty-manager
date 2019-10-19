package org.thehellnet.lanparty.manager.model.dto.service;

public class GameMapServiceDTO extends ServiceDTO {

    public String tag;
    public String name;
    public Long gameId;
    public Boolean stock;

    public GameMapServiceDTO() {
    }

    public GameMapServiceDTO(String tag, String name, Long gameId, Boolean stock) {
        this.tag = tag;
        this.name = name;
        this.gameId = gameId;
        this.stock = stock;
    }
}
