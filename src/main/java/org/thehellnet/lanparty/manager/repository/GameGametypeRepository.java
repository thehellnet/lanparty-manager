package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.GameGametype;
import org.thehellnet.lanparty.manager.model.persistence.Gametype;

@Repository
public interface GameGametypeRepository extends JpaRepository<GameGametype, Long> {

    GameGametype findByGameAndGametype(Game game, Gametype gametype);
}
