package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    Match findByName(String name);
}
