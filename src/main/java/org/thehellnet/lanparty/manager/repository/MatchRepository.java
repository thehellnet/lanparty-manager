package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Match;
import org.thehellnet.lanparty.manager.model.persistence.Team;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    Match findByName(String name);

    Match findByTournamentAndLocalTeamAndGuestTeamAndLevel(Tournament tournament, Team localTeam, Team guestTeam, int level);

    Match findByTournamentAndLevel(Tournament tournament, int level);
}
