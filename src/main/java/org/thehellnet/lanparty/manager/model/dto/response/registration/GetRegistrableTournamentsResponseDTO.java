package org.thehellnet.lanparty.manager.model.dto.response.registration;

import java.time.LocalDateTime;
import java.util.List;

public class GetRegistrableTournamentsResponseDTO extends RegistrationResponseDTO {

    public static final class TournamentDTO {
        private final Long id;
        private final String name;
        private final String gameName;
        private final LocalDateTime startTs;
        private final LocalDateTime endTs;
        private final LocalDateTime startRegistrationTs;
        private final LocalDateTime endRegistrationTs;

        public TournamentDTO(Long id,
                             String name,
                             String gameName,
                             LocalDateTime startTs,
                             LocalDateTime endTs,
                             LocalDateTime startRegistrationTs,
                             LocalDateTime endRegistrationTs) {
            this.id = id;
            this.name = name;
            this.gameName = gameName;
            this.startTs = startTs;
            this.endTs = endTs;
            this.startRegistrationTs = startRegistrationTs;
            this.endRegistrationTs = endRegistrationTs;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getGameName() {
            return gameName;
        }

        public LocalDateTime getStartTs() {
            return startTs;
        }

        public LocalDateTime getEndTs() {
            return endTs;
        }

        public LocalDateTime getStartRegistrationTs() {
            return startRegistrationTs;
        }

        public LocalDateTime getEndRegistrationTs() {
            return endRegistrationTs;
        }
    }

    private final TournamentDTO[] tournaments;

    public GetRegistrableTournamentsResponseDTO(List<TournamentDTO> tournaments) {
        this(tournaments.toArray(new TournamentDTO[0]));
    }

    public GetRegistrableTournamentsResponseDTO(TournamentDTO[] tournaments) {
        this.tournaments = tournaments;
    }

    public TournamentDTO[] getTournaments() {
        return tournaments;
    }
}
