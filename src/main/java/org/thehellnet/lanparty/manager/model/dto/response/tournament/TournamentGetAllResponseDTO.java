package org.thehellnet.lanparty.manager.model.dto.response.tournament;

import org.thehellnet.lanparty.manager.model.dto.light.TournamentLight;
import org.thehellnet.lanparty.manager.model.dto.response.ResponseDTO;

import java.util.List;

public class TournamentGetAllResponseDTO extends ResponseDTO {

    List<TournamentLight> tournaments;

    public TournamentGetAllResponseDTO() {
    }

    public TournamentGetAllResponseDTO(List<TournamentLight> tournaments) {
        this.tournaments = tournaments;
    }

    public List<TournamentLight> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<TournamentLight> tournaments) {
        this.tournaments = tournaments;
    }
}
