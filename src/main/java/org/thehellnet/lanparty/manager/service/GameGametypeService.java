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
import org.thehellnet.lanparty.manager.repository.GameRepository;
import org.thehellnet.lanparty.manager.repository.GametypeRepository;

import java.util.List;

@Service
public class GameGametypeService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final GameGametypeRepository gameGametypeRepository;
    private final GameRepository gameRepository;
    private final GametypeRepository gametypeRepository;

    @Autowired
    public GameGametypeService(GameGametypeRepository gameGametypeRepository, GameRepository gameRepository, GametypeRepository gametypeRepository) {
        this.gameGametypeRepository = gameGametypeRepository;
        this.gameRepository = gameRepository;
        this.gametypeRepository = gametypeRepository;
    }

    @Transactional
    public GameGametype create(Long gameId, Long gametypeId, String tag) {
        if (gameId == null) {
            throw new InvalidDataException("Invalid game");
        }
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            throw new InvalidDataException("Game not found");
        }

        if (gametypeId == null) {
            throw new InvalidDataException("Invalid gametype");
        }
        Gametype gametype = gametypeRepository.findById(gametypeId).orElse(null);
        if (gametype == null) {
            throw new InvalidDataException("Gametype not found");
        }

        if (tag == null || tag.strip().length() == 0) {
            throw new InvalidDataException("Invalid tag");
        }

        GameGametype gameGametype = new GameGametype(game, gametype, tag.strip());
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
    public GameGametype update(Long id, Long gameId, Long gametypeId, String tag) {
        GameGametype gameGametype = findById(id);

        boolean changed = false;

        if (gameId != null) {
            Game game = gameRepository.findById(gameId).orElse(null);
            if (game == null) {
                throw new InvalidDataException("Invalid game");
            }
            gameGametype.setGame(game);
            changed = true;
        }

        if (gametypeId != null) {
            Gametype gametype = gametypeRepository.findById(gametypeId).orElse(null);
            if (gametype == null) {
                throw new InvalidDataException("Invalid gametype");
            }
            gameGametype.setGametype(gametype);
            changed = true;
        }

        if (tag != null && tag.strip().length() > 0) {
            gameGametype.setTag(tag.strip());
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
