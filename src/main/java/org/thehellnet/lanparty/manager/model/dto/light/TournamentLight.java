package org.thehellnet.lanparty.manager.model.dto.light;

import org.thehellnet.lanparty.manager.model.persistence.Tournament;

public class TournamentLight {

    private String name;
    private String gameTag;
    private String statusName;

    public TournamentLight() {
    }

    public TournamentLight(String name, String gameTag, String statusName) {
        this.name = name;
        this.gameTag = gameTag;
        this.statusName = statusName;
    }

    public TournamentLight(Tournament tournament) {
        this.name = tournament.getName();
        this.gameTag = tournament.getGame().getTag();
        this.statusName = tournament.getStatus().name();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameTag() {
        return gameTag;
    }

    public void setGameTag(String gameTag) {
        this.gameTag = gameTag;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
