package org.thehellnet.lanparty.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.GameFile;

import java.util.List;

@Repository
public interface GameFileRepository extends JpaRepository<GameFile, Long> {

    GameFile findByGameAndFilename(Game game, String filename);

    List<GameFile> findAllByGame(Game game);
}
