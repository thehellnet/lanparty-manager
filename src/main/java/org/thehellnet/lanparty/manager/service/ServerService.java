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
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.repository.GameRepository;
import org.thehellnet.lanparty.manager.repository.ServerRepository;

import java.util.List;

@Service
public class ServerService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final ServerRepository serverRepository;
    private final GameRepository gameRepository;

    @Autowired
    public ServerService(ServerRepository serverRepository, GameRepository gameRepository) {
        this.serverRepository = serverRepository;
        this.gameRepository = gameRepository;
    }

    @Transactional
    public Server create(String tag, String name, Long gameId, String address, Integer port, String rconPassword, String logFile, Boolean logParsingEnabled) {
        if (tag == null) {
            throw new InvalidDataException("Invalid tag");
        }

        if (gameId == null) {
            throw new InvalidDataException("Invalid game");
        }
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            throw new InvalidDataException("Game not found");
        }

        if (address == null) {
            throw new InvalidDataException("Invalid address");
        }

        if (port == null) {
            throw new InvalidDataException("Invalid port");
        }

        if (logParsingEnabled == null) {
            throw new InvalidDataException("Invalid logParsingEnabled");
        }

        Server server = new Server(tag, name, game, address, port, rconPassword, logFile, logParsingEnabled);
        server = serverRepository.save(server);
        return server;
    }

    @Transactional(readOnly = true)
    public Server read(Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public List<Server> readAll() {
        return serverRepository.findAll();
    }

    @Transactional
    public Server update(Long id, String tag, String name, Long gameId, String address, Integer port, String rconPassword, String logFile, Boolean logParsingEnabled) {
        Server server = findById(id);

        boolean changed = false;

        if (tag != null) {
            server.setTag(tag);
            changed = true;
        }

        if (name != null) {
            server.setName(name);
            changed = true;
        }

        if (gameId != null) {
            Game game = gameRepository.findById(gameId).orElse(null);
            if (game == null) {
                throw new InvalidDataException("Invalid game");
            }
            server.setGame(game);
            changed = true;
        }

        if (address != null) {
            server.setAddress(address);
            changed = true;
        }

        if (port != null) {
            server.setPort(port);
            changed = true;
        }

        if (rconPassword != null) {
            server.setRconPassword(rconPassword);
            changed = true;
        }

        if (logFile != null) {
            server.setLogFile(logFile);
            changed = true;
        }

        if (logParsingEnabled != null) {
            server.setLogParsingEnabled(logParsingEnabled);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return serverRepository.save(server);
    }

    @Transactional
    public void delete(Long id) {
        Server server = findById(id);
        serverRepository.delete(server);
    }

    @Transactional(readOnly = true)
    public Server findById(Long id) {
        Server server = serverRepository.findById(id).orElse(null);
        if (server == null) {
            throw new NotFoundException();
        }
        return server;
    }
}
