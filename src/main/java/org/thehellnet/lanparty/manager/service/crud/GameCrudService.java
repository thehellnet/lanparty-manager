package org.thehellnet.lanparty.manager.service.crud;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.dto.service.GameServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.repository.GameRepository;

@Service
public class GameCrudService extends AbstractCrudService<Game, GameServiceDTO, GameRepository> {

    public GameCrudService(GameRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public Game create(GameServiceDTO dto) {
        if (dto.tag == null) {
            throw new InvalidDataException("Invalid player");
        }
        if (dto.name == null) {
            throw new InvalidDataException("Invalid game");
        }

        Game game = new Game(dto.tag, dto.name);
        game = repository.save(game);
        return game;
    }

    @Override
    @Transactional
    public Game update(Long id, GameServiceDTO dto) {
        Game game = findById(id);

        boolean changed = false;

        if (dto.tag != null) {
            game.setTag(dto.tag);
            changed = true;
        }

        if (dto.name != null) {
            game.setName(dto.name);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return repository.save(game);
    }

    @Transactional(readOnly = true)
    public Game findByTag(String gameTag) {
        return repository.findByTag(gameTag);
    }
}
