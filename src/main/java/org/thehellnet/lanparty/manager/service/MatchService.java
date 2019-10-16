package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.persistence.*;
import org.thehellnet.lanparty.manager.repository.MatchRepository;

import java.util.List;

@Service
public class MatchService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final MatchRepository matchRepository;

    @Autowired
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Transactional
    public Match create(String name, Tournament tournament, Server server, GameMap gameMap, Gametype gametype, Team localTeam, Team guestTeam) {
        if (name == null) {
            throw new InvalidDataException("Invalid tournament");
        }
        if (tournament == null) {
            throw new InvalidDataException("Invalid tournament");
        }
        if (server == null) {
            throw new InvalidDataException("Invalid tournament");
        }

        Match match = new Match(name, tournament, server, gameMap, gametype, localTeam, guestTeam);
        match = matchRepository.save(match);
        return match;
    }

    @Transactional(readOnly = true)
    public Match get(Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public List<Match> getAll() {
        return matchRepository.findAll();
    }

    @Transactional
    public Match update(Long id, String name, Tournament tournament, Server server, GameMap gameMap, Gametype gametype, Team localTeam, Team guestTeam) {
        Match match = findById(id);

        boolean changed = false;

        if (name != null) {
            match.setName(name);
            changed = true;
        }

        if (tournament != null) {
            match.setTournament(tournament);
            changed = true;
        }

        if (server != null) {
            match.setServer(server);
            changed = true;
        }

        if (gameMap != null) {
            match.setGameMap(gameMap);
            changed = true;
        }

        if (gametype != null) {
            match.setGametype(gametype);
            changed = true;
        }

        if (localTeam != null) {
            match.setLocalTeam(localTeam);
            changed = true;
        }

        if (guestTeam != null) {
            match.setGuestTeam(guestTeam);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return matchRepository.save(match);
    }

    @Transactional
    public void delete(Long id) {
        Match match = findById(id);
        matchRepository.delete(match);
    }

    @Transactional(readOnly = true)
    public Match findById(Long id) {
        Match match = matchRepository.findById(id).orElse(null);
        if (match == null) {
            throw new NotFoundException();
        }
        return match;
    }
}
