package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Player;
import org.thehellnet.lanparty.manager.model.persistence.Seat;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    Seat findByIpAddress(String ipAddress);

    Seat findByNameAndTournament(String name, Tournament tournament);

    Seat findByGuidAndTournament(String guid, Tournament tournament);

    List<Seat> findAllByPlayer(Player player);
}
