package org.thehellnet.lanparty.manager.model.dto.request.crud.save;

public class TournamentSaveCrudRequestDTO extends SaveCrudRequestDTO {

    private String name;
    private Long gameId;
    private String statusName;
    private String cfg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getCfg() {
        return cfg;
    }

    public void setCfg(String cfg) {
        this.cfg = cfg;
    }
}
