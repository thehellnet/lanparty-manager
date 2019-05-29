package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    Seat findByIpAddress(String ipAddress);
}
