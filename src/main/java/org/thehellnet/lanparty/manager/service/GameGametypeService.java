package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.GameGametype;
import org.thehellnet.lanparty.manager.model.persistence.Gametype;
import org.thehellnet.lanparty.manager.repository.GameGametypeRepository;

import java.util.List;

@Service
public class GameGametypeService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final GameGametypeRepository gameGametypeRepository;

    @Autowired
    public GameGametypeService(GameGametypeRepository gameGametypeRepository) {
        this.gameGametypeRepository = gameGametypeRepository;
    }

    @Transactional
    public GameGametype create(Game game, Gametype gametype, String tag) {
        if (game == null) {
            throw new InvalidDataException("Invalid name");
        }
        if (gametype == null) {
            throw new InvalidDataException("Invalid tournament");
        }
        if (tag == null) {
            throw new InvalidDataException("Invalid tournament");
        }

        GameGametype gameGametype = new GameGametype(game, gametype, tag);
        gameGametype = gameGametypeRepository.save(gameGametype);
        return gameGametype;
    }

    @Transactional(readOnly = true)
    public GameGametype get(Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public List<GameGametype> getAll() {
        return gameGametypeRepository.findAll();
    }

    @Transactional
    public GameGametype update(Long id, Game game, Gametype gametype, String tag) {
        GameGametype gameGametype = findById(id);

        boolean changed = false;

        if (game != null) {
            gameGametype.setGame(game);
            changed = true;
        }

        if (gametype != null) {
            gameGametype.setGametype(gametype);
            changed = true;
        }

        if (tag != null) {
            gameGametype.setTag(tag);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return gameGametypeRepository.save(gameGametype);
    }

    @Transactional
    public void delete(Long id) {
        GameGametype gameGametype = findById(id);
        gameGametypeRepository.delete(gameGametype);
    }

    @Transactional(readOnly = true)
    public GameGametype findById(Long id) {
        GameGametype gameGametype = gameGametypeRepository.findById(id).orElse(null);
        if (gameGametype == null) {
            throw new NotFoundException();
        }
        return gameGametype;
    }
}
