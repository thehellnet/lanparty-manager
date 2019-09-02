package org.thehellnet.lanparty.manager.model.dto.request.crud.create;

public class TournamentCreateCrudRequestDTO extends CreateCrudRequestDTO {

    private String name;
    private Long game;

    public TournamentCreateCrudRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGame() {
        return game;
    }

    public void setGame(Long game) {
        this.game = game;
    }
}
