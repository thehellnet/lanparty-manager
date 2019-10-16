package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.GameRepository;
import org.thehellnet.lanparty.manager.repository.TournamentRepository;

import java.util.List;

@Service
public class TournamentService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(TournamentService.class);

    private final TournamentRepository tournamentRepository;
    private final GameRepository gameRepository;

    public TournamentService(TournamentRepository tournamentRepository, GameRepository gameRepository) {
        this.tournamentRepository = tournamentRepository;
        this.gameRepository = gameRepository;
    }

    @Transactional
    public Tournament create(String name, Long gameId, String cfg) {
        if (name == null) {
            throw new InvalidDataException("Invalid name");
        }

        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            throw new InvalidDataException("Invalid game");
        }

        if (cfg == null) {
            throw new InvalidDataException("Invalid cfg");
        }

        Tournament tournament = new Tournament(name, game, cfg);
        tournament = tournamentRepository.save(tournament);
        return tournament;
    }

    @Transactional(readOnly = true)
    public Tournament get(Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }

    @Transactional
    public Tournament update(Long id, String name, Long gameId, String cfg) {
        Tournament tournament = findById(id);

        boolean changed = false;

        if (name != null) {
            tournament.setName(name);
            changed = true;
        }

        if (gameId != null) {
            Game game = gameRepository.findById(gameId).orElse(null);
            if (game == null) {
                throw new InvalidDataException("Invalid game");
            }

            tournament.setGame(game);
            changed = true;
        }

        if (cfg != null) {
            tournament.setCfg(cfg);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return tournamentRepository.save(tournament);
    }

    @Transactional
    public void delete(Long id) {
        Tournament tournament = findById(id);
        tournamentRepository.delete(tournament);
    }

    @Transactional(readOnly = true)
    public Tournament findById(Long id) {
        Tournament tournament = tournamentRepository.findById(id).orElse(null);
        if (tournament == null) {
            throw new NotFoundException();
        }
        return tournament;
    }
}
