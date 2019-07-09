package org.thehellnet.lanparty.manager.model.dto.request.crud.create;

public class TournamentCreateCrudRequestDTO extends CreateCrudRequestDTO {

    private String name;
    private Long gameId;

    public TournamentCreateCrudRequestDTO() {
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
}
