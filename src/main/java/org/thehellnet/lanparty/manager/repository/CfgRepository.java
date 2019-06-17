package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Cfg;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.Player;

@Repository
public interface CfgRepository extends JpaRepository<Cfg, Long> {

    Cfg findByPlayerAndGame(Player player, Game game);
}
