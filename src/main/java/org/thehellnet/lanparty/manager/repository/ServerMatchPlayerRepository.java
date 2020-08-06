package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.ServerMatch;
import org.thehellnet.lanparty.manager.model.persistence.ServerMatchPlayer;

import java.util.List;

@Repository
public interface ServerMatchPlayerRepository extends JpaRepository<ServerMatchPlayer, Long> {

    List<ServerMatchPlayer> findAllByServerMatch(ServerMatch serverMatch);

    ServerMatchPlayer findByServerMatchAndGuidAndNum(ServerMatch serverMatch, String guid, int num);
}
