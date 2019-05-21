package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Game findByTag(String tag);
}
