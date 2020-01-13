package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    Tournament findByName(String name);

    @Query("SELECT t " +
            "FROM Tournament t " +
            "WHERE t.enabled = TRUE " +
            "AND t.registrationEnabled = TRUE " +
            "ORDER BY t.startDateTime ASC")
    List<Tournament> findRegistrables();
}
