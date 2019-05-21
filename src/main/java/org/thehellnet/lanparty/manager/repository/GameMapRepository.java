package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.GameMap;

import java.util.List;

@Repository
public interface GameMapRepository extends JpaRepository<GameMap, Long> {

    GameMap findByTagAndGame(String tag, Game game);

    List<GameMap> findByGame(Game game);
}
