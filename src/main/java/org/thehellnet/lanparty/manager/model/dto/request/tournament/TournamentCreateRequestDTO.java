package org.thehellnet.lanparty.manager.model.dto.request.tournament;

import org.thehellnet.lanparty.manager.model.dto.request.RequestDTO;

public class TournamentCreateRequestDTO extends RequestDTO {

    private String name;
    private String gameTag;

    public TournamentCreateRequestDTO() {
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
}
