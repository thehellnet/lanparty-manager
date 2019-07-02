package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    Tournament findByName(String name);
}
