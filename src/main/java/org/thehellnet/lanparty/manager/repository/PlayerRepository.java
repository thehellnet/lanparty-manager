package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByBarcode(String barcode);
}
