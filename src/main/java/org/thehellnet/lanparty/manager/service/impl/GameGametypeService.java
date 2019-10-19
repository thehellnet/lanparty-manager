package org.thehellnet.lanparty.manager.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.dto.service.GameGametypeServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.GameGametype;
import org.thehellnet.lanparty.manager.model.persistence.Gametype;
import org.thehellnet.lanparty.manager.repository.GameGametypeRepository;
import org.thehellnet.lanparty.manager.repository.GameRepository;
import org.thehellnet.lanparty.manager.repository.GametypeRepository;
import org.thehellnet.lanparty.manager.service.AbstractCrudService;
import org.thehellnet.lanparty.manager.service.TeamService;

@Service
public class GameGametypeService extends AbstractCrudService<GameGametype, GameGametypeServiceDTO, GameGametypeRepository> {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final GameRepository gameRepository;
    private final GametypeRepository gametypeRepository;

    public GameGametypeService(GameGametypeRepository repository, GameRepository gameRepository, GametypeRepository gametypeRepository) {
        super(repository);
        this.gameRepository = gameRepository;
        this.gametypeRepository = gametypeRepository;
    }

    @Override
    @Transactional
    public GameGametype create(GameGametypeServiceDTO dto) {
        if (dto.gameId == null) {
            throw new InvalidDataException("Invalid game");
        }
        Game game = gameRepository.findById(dto.gameId).orElse(null);
        if (game == null) {
            throw new InvalidDataException("Game not found");
        }

        if (dto.gametypeId == null) {
            throw new InvalidDataException("Invalid gametype");
        }
        Gametype gametype = gametypeRepository.findById(dto.gametypeId).orElse(null);
        if (gametype == null) {
            throw new InvalidDataException("Gametype not found");
        }

        if (dto.tag == null || dto.tag.strip().length() == 0) {
            throw new InvalidDataException("Invalid tag");
        }

        GameGametype gameGametype = new GameGametype(game, gametype, dto.tag.strip());
        gameGametype = repository.save(gameGametype);
        return gameGametype;
    }

    @Override
    @Transactional
    public GameGametype update(Long id, GameGametypeServiceDTO dto) {
        GameGametype gameGametype = findById(id);

        boolean changed = false;

        if (dto.gameId != null) {
            Game game = gameRepository.findById(dto.gameId).orElse(null);
            if (game == null) {
                throw new InvalidDataException("Invalid game");
            }
            gameGametype.setGame(game);
            changed = true;
        }

        if (dto.gametypeId != null) {
            Gametype gametype = gametypeRepository.findById(dto.gametypeId).orElse(null);
            if (gametype == null) {
                throw new InvalidDataException("Invalid gametype");
            }
            gameGametype.setGametype(gametype);
            changed = true;
        }

        if (dto.tag != null && dto.tag.strip().length() > 0) {
            gameGametype.setTag(dto.tag.strip());
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return repository.save(gameGametype);
    }
}
