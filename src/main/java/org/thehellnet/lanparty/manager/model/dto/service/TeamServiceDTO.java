package org.thehellnet.lanparty.manager.model.dto.service;

public class TeamServiceDTO extends ServiceDTO {

    public String name;
    public Long tournamentId;

    public TeamServiceDTO() {
    }

    public TeamServiceDTO(String name, Long tournamentId) {
        this.name = name;
        this.tournamentId = tournamentId;
    }
}
