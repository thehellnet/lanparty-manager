package org.thehellnet.lanparty.manager.model.dto.response.tournament;

import org.thehellnet.lanparty.manager.model.dto.response.ResponseDTO;

import java.util.List;

public class TournamentListResponseDTO extends ResponseDTO {

    public static class TournamentDTO {
        private Long id;
        private String name;
        private String gameTag;

        public TournamentDTO() {
        }

        public TournamentDTO(Long id, String name, String gameTag) {
            this.id = id;
            this.name = name;
            this.gameTag = gameTag;
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

        public String getGameTag() {
            return gameTag;
        }

        public void setGameTag(String gameTag) {
            this.gameTag = gameTag;
        }
    }

    private List<TournamentDTO> tournaments;

    public TournamentListResponseDTO() {
    }

    public TournamentListResponseDTO(List<TournamentDTO> tournaments) {
        this.tournaments = tournaments;
    }

    public List<TournamentDTO> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<TournamentDTO> tournaments) {
        this.tournaments = tournaments;
    }
}
