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
import org.thehellnet.lanparty.manager.model.persistence.GameMap;
import org.thehellnet.lanparty.manager.repository.GameMapRepository;
import org.thehellnet.lanparty.manager.repository.GameRepository;

import java.util.List;

@Service
public class GameMapService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final GameMapRepository gameMapRepository;
    private final GameRepository gameRepository;

    @Autowired
    public GameMapService(GameMapRepository gameMapRepository, GameRepository gameRepository) {
        this.gameMapRepository = gameMapRepository;
        this.gameRepository = gameRepository;
    }

    @Transactional
    public GameMap create(String tag, String name, Long gameId, Boolean stock) {
        if (tag == null) {
            throw new InvalidDataException("Invalid tag");
        }

        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            throw new InvalidDataException("Invalid game");
        }

        if (stock == null) {
            throw new InvalidDataException("Invalid stock");
        }

        GameMap gameMap = new GameMap(tag, name, game, stock);
        gameMap = gameMapRepository.save(gameMap);
        return gameMap;
    }

    @Transactional(readOnly = true)
    public GameMap get(Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public List<GameMap> getAll() {
        return gameMapRepository.findAll();
    }

    @Transactional
    public GameMap update(Long id, String tag, String name, Long gameId, Boolean stock) {
        GameMap gameMap = findById(id);

        boolean changed = false;

        if (tag != null) {
            gameMap.setTag(tag);
            changed = true;
        }

        if (name != null) {
            gameMap.setName(name);
            changed = true;
        }

        if (gameId != null) {
            Game game = gameRepository.findById(gameId).orElse(null);
            if (game == null) {
                throw new InvalidDataException("Invalid game");
            }
            gameMap.setGame(game);
            changed = true;
        }

        if (stock != null) {
            gameMap.setStock(stock);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return gameMapRepository.save(gameMap);
    }

    @Transactional
    public void delete(Long id) {
        GameMap gameMap = findById(id);
        gameMapRepository.delete(gameMap);
    }

    @Transactional(readOnly = true)
    public GameMap findById(Long id) {
        GameMap gameMap = gameMapRepository.findById(id).orElse(null);
        if (gameMap == null) {
            throw new NotFoundException();
        }
        return gameMap;
    }
}
