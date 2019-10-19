package org.thehellnet.lanparty.manager.model.dto.service;

public class GameServiceDTO extends ServiceDTO {

    public String tag;
    public String name;

    public GameServiceDTO() {
    }

    public GameServiceDTO(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }
}
