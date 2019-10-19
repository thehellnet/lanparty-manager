package org.thehellnet.lanparty.manager.service.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.dto.service.GameMapServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.GameMap;
import org.thehellnet.lanparty.manager.repository.GameMapRepository;
import org.thehellnet.lanparty.manager.repository.GameRepository;

@Service
public class GameMapCrudService extends AbstractCrudService<GameMap, GameMapServiceDTO, GameMapRepository> {

    private static final Logger logger = LoggerFactory.getLogger(GameMapCrudService.class);

    private final GameRepository gameRepository;

    public GameMapCrudService(GameMapRepository repository, GameRepository gameRepository) {
        super(repository);
        this.gameRepository = gameRepository;
    }

    @Override
    @Transactional
    public GameMap create(GameMapServiceDTO dto) {
        if (dto.tag == null) {
            throw new InvalidDataException("Invalid tag");
        }

        if (dto.gameId == null) {
            throw new InvalidDataException("Invalid game");
        }
        Game game = gameRepository.findById(dto.gameId).orElse(null);
        if (game == null) {
            throw new InvalidDataException("Game not found");
        }

        if (dto.stock == null) {
            throw new InvalidDataException("Invalid stock");
        }

        GameMap gameMap = new GameMap(dto.tag, dto.name, game, dto.stock);
        gameMap = repository.save(gameMap);
        return gameMap;
    }

    @Override
    @Transactional
    public GameMap update(Long id, GameMapServiceDTO dto) {
        GameMap gameMap = findById(id);

        boolean changed = false;

        if (dto.tag != null) {
            gameMap.setTag(dto.tag);
            changed = true;
        }

        if (dto.name != null) {
            gameMap.setName(dto.name);
            changed = true;
        }

        if (dto.gameId != null) {
            Game game = gameRepository.findById(dto.gameId).orElse(null);
            if (game == null) {
                throw new InvalidDataException("Invalid game");
            }
            gameMap.setGame(game);
            changed = true;
        }

        if (dto.stock != null) {
            gameMap.setStock(dto.stock);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return repository.save(gameMap);
    }
}
