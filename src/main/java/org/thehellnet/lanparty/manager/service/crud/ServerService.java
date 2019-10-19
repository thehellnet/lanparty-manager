package org.thehellnet.lanparty.manager.service.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.dto.service.ServerServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.repository.GameRepository;
import org.thehellnet.lanparty.manager.repository.ServerRepository;

@Service
public class ServerService extends AbstractCrudService<Server, ServerServiceDTO, ServerRepository> {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final GameRepository gameRepository;

    public ServerService(ServerRepository repository, GameRepository gameRepository) {
        super(repository);
        this.gameRepository = gameRepository;
    }

    @Override
    @Transactional
    public Server create(ServerServiceDTO dto) {
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

        if (dto.address == null) {
            throw new InvalidDataException("Invalid address");
        }

        if (dto.port == null) {
            throw new InvalidDataException("Invalid port");
        }

        if (dto.logParsingEnabled == null) {
            throw new InvalidDataException("Invalid logParsingEnabled");
        }

        Server server = new Server(dto.tag, dto.name, game, dto.address, dto.port, dto.rconPassword, dto.logFile, dto.logParsingEnabled);
        server = repository.save(server);
        return server;
    }

    @Override
    @Transactional
    public Server update(Long id, ServerServiceDTO dto) {
        Server server = findById(id);

        boolean changed = false;

        if (dto.tag != null) {
            server.setTag(dto.tag);
            changed = true;
        }

        if (dto.name != null) {
            server.setName(dto.name);
            changed = true;
        }

        if (dto.gameId != null) {
            Game game = gameRepository.findById(dto.gameId).orElse(null);
            if (game == null) {
                throw new InvalidDataException("Invalid game");
            }
            server.setGame(game);
            changed = true;
        }

        if (dto.address != null) {
            server.setAddress(dto.address);
            changed = true;
        }

        if (dto.port != null) {
            server.setPort(dto.port);
            changed = true;
        }

        if (dto.rconPassword != null) {
            server.setRconPassword(dto.rconPassword);
            changed = true;
        }

        if (dto.logFile != null) {
            server.setLogFile(dto.logFile);
            changed = true;
        }

        if (dto.logParsingEnabled != null) {
            server.setLogParsingEnabled(dto.logParsingEnabled);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return repository.save(server);
    }
}
