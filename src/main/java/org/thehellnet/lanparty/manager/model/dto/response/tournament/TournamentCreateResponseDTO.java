package org.thehellnet.lanparty.manager.model.dto.response.tournament;

import org.thehellnet.lanparty.manager.model.dto.response.ResponseDTO;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;

public class TournamentCreateResponseDTO extends ResponseDTO {

    private Tournament tournament;

    public TournamentCreateResponseDTO() {
    }

    public TournamentCreateResponseDTO(Tournament tournament) {
        this.tournament = tournament;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
}
