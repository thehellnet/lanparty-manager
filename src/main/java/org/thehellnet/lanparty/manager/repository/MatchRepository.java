package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Match;
import org.thehellnet.lanparty.manager.model.persistence.Team;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    Match findByName(String name);

    @Query("SELECT m " +
            "FROM Match m " +
            "WHERE m.tournament = :tournament " +
            "ORDER BY m.playOrder, " +
            "  m.startTs")
    List<Match> findAllByTournament(@Param("tournament") Tournament tournament);

    Match findByTournamentAndLocalTeamAndGuestTeamAndLevel(Tournament tournament, Team localTeam, Team guestTeam, int level);

    Match findByTournamentAndLevel(Tournament tournament, int level);
}
