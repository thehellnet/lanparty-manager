package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.model.persistence.ServerMatch;

import java.util.List;

@Repository
public interface ServerMatchRepository extends JpaRepository<ServerMatch, Long> {

    List<ServerMatch> findAllByServerOrderByStartTsDesc(Server server);

    @Query("SELECT sm " +
            "FROM ServerMatch sm " +
            "WHERE sm.server = :server " +
            "  AND sm.endTs IS NULL")
    List<ServerMatch> findAllRunningByServer(@Param("server") Server server);

    ServerMatch findFirstByServerAndEndTsNullOrderByStartTsDesc(Server server);
}
