package org.thehellnet.lanparty.manager.model.dto.light;

import org.thehellnet.lanparty.manager.model.persistence.Tournament;

public class TournamentLight {

    private Long id;
    private String name;
    private Long gameId;
    private String statusName;

    public TournamentLight() {
    }

    public TournamentLight(Tournament tournament) {
        this.id = tournament.getId();
        this.name = tournament.getName();
        this.gameId = tournament.getGame().getId();
        this.statusName = tournament.getStatus().name();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
