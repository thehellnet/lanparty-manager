package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.Gametype;

import java.util.List;

@Repository
public interface GametypeRepository extends JpaRepository<Gametype, Long> {

    Gametype findByName(String name);

    @Query("SELECT gt " +
            "FROM Gametype gt " +
            "  JOIN GameGametype ggt ON gt = ggt.gametype " +
            "WHERE ggt.game = :game")
    List<Gametype> findByGame(@Param("game") Game game);

    @Query("SELECT gt " +
            "FROM Gametype gt " +
            "  JOIN GameGametype ggt ON gt = ggt.gametype " +
            "WHERE ggt.game = :game " +
            "  AND ggt.tag = :tag")
    Gametype findByGameAndTag(@Param("game") Game game, @Param("tag") String tag);
}
