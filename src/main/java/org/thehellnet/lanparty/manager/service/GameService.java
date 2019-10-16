package org.thehellnet.lanparty.manager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.repository.GameRepository;

import java.util.List;

@Service
public class GameService extends AbstractService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Transactional
    public Game create(String tag, String name) {
        if (tag == null) {
            throw new InvalidDataException("Invalid player");
        }
        if (name == null) {
            throw new InvalidDataException("Invalid game");
        }

        Game game = new Game(tag, name);
        game = gameRepository.save(game);
        return game;
    }

    @Transactional(readOnly = true)
    public Game get(Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public List<Game> getAll() {
        return gameRepository.findAll();
    }

    @Transactional
    public Game update(Long id, String tag, String name) {
        Game game = findById(id);

        boolean changed = false;

        if (tag != null) {
            game.setTag(tag);
            changed = true;
        }

        if (name != null) {
            game.setName(name);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return gameRepository.save(game);
    }

    @Transactional
    public void delete(Long id) {
        Game game = findById(id);
        gameRepository.delete(game);
    }

    @Transactional(readOnly = true)
    public Game findById(Long id) {
        Game game = gameRepository.findById(id).orElse(null);
        if (game == null) {
            throw new NotFoundException();
        }
        return game;
    }

    @Transactional(readOnly = true)
    public Game findByTag(String gameTag) {
        return gameRepository.findByTag(gameTag);
    }
}
