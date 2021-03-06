package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Server;

import java.util.List;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {

    Server findByTag(String tag);

    Server findByAddressAndPort(String address, Integer port);

    List<Server> findAllByLogParsingEnabledIsTrue();

    @Query("SELECT s " +
            "FROM Server s " +
            "WHERE s.serverBinaryEnabled = TRUE " +
            "  AND s.serverBinary IS NOT NULL")
    List<Server> findAllServerBinaryEnabled();
}
