package org.thehellnet.lanparty.manager.model.dto.service;

public class TournamentServiceDTO extends ServiceDTO {

    public String name;
    public Long gameId;
    public String cfg;

    public TournamentServiceDTO() {
    }

    public TournamentServiceDTO(String name, Long gameId, String cfg) {
        this.name = name;
        this.gameId = gameId;
        this.cfg = cfg;
    }
}
