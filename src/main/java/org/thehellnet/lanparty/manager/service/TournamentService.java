package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.game.GameNotFoundException;
import org.thehellnet.lanparty.manager.exception.tournament.TournamentAlreadyExistsException;
import org.thehellnet.lanparty.manager.exception.tournament.TournamentInvalidNameException;
import org.thehellnet.lanparty.manager.exception.tournament.TournamentNotFoundException;
import org.thehellnet.lanparty.manager.model.constant.TournamentStatus;
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

    @Transactional(readOnly = true)
    public List<Tournament> getAll() {
        List<Tournament> tournamentList = tournamentRepository.findAll();
        for (Tournament tournament : tournamentList) {
            tournament.getGame();
        }
        return tournamentList;
    }

    @Transactional(readOnly = true)
    public Tournament get(Long id) {
        Tournament tournament = tournamentRepository.findById(id).orElse(null);
        tournament.getGame();
        return tournament;
    }

    @Transactional
    public Tournament create(String name, Long gameId) throws GameNotFoundException, TournamentInvalidNameException, TournamentAlreadyExistsException {
        if (name == null || name.length() == 0) {
            throw new TournamentInvalidNameException();
        }

        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            throw new GameNotFoundException();
        }

        Tournament tournament = tournamentRepository.findByName(name);
        if (tournament != null) {
            throw new TournamentAlreadyExistsException();
        }

        tournament = new Tournament(name, game);
        tournament = tournamentRepository.save(tournament);
        return tournament;
    }

    @Transactional
    public Tournament save(Long id, String name, Long gameId, String statusName, String cfg) throws TournamentNotFoundException, GameNotFoundException {
        Tournament tournament = tournamentRepository.findById(id).orElse(null);
        if (tournament == null) {
            throw new TournamentNotFoundException();
        }

        if (name != null && name.length() > 0) {
            tournament.setName(name);
        }

        if (gameId != null) {
            Game game = gameRepository.findById(gameId).orElse(null);
            if (game == null) {
                throw new GameNotFoundException();
            }

            tournament.setGame(game);
        }

        if (statusName != null && statusName.length() > 0) {
            tournament.setStatus(TournamentStatus.valueOf(statusName));
        }

        if (cfg != null) {
            tournament.setCfg(cfg);
        }

        return tournamentRepository.save(tournament);
    }

    @Transactional
    public void delete(Long id) throws TournamentNotFoundException {
        Tournament tournament = tournamentRepository.findById(id).orElse(null);
        if (tournament == null) {
            throw new TournamentNotFoundException();
        }

        tournamentRepository.delete(tournament);
    }

    @Transactional(readOnly = true)
    public Tournament findByName(String name) {
        return tournamentRepository.findByName(name);
    }
}
