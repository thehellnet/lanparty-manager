package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.model.persistence.Spectator;

import java.util.List;

@Repository
public interface SpectatorRepository extends JpaRepository<Spectator, Long> {

    List<Spectator> findAllByActiveIsTrue();

    List<Spectator> findAllByServer(Server server);
}
