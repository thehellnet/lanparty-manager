package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Player;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByNickname(String nickname);

    @Query("SELECT p FROM Player p WHERE p.appUser = :appUser AND p.team.tournament = :tournament")
    Player findByAppUserAndTournament(@Param("appUser") AppUser appUser, @Param("tournament") Tournament tournament);

    Player findByTeamTournamentAndNickname(Tournament tournament, String nickname);
}
