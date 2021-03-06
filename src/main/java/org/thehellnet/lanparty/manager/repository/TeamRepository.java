package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Team;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Team findByName(String name);

    List<Team> findAllByTournament(Tournament tournament);
}
