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
import org.thehellnet.lanparty.manager.repository.*;

import java.util.List;

@Service
public class MatchService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final ServerRepository serverRepository;
    private final GameMapRepository gameMapRepository;
    private final GametypeRepository gametypeRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public MatchService(MatchRepository matchRepository, TournamentRepository tournamentRepository, ServerRepository serverRepository, GameMapRepository gameMapRepository, GametypeRepository gametypeRepository, TeamRepository teamRepository) {
        this.matchRepository = matchRepository;
        this.tournamentRepository = tournamentRepository;
        this.serverRepository = serverRepository;
        this.gameMapRepository = gameMapRepository;
        this.gametypeRepository = gametypeRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    public Match create(String name, Long tournamentId, Long serverId, Long gameMapId, Long gametypeId, Long localTeamId, Long guestTeamId) {
        if (name == null) {
            throw new InvalidDataException("Invalid name");
        }

        if (tournamentId == null) {
            throw new InvalidDataException("Invalid tournament");
        }
        Tournament tournament = tournamentRepository.findById(tournamentId).orElse(null);
        if (tournament == null) {
            throw new InvalidDataException("Tournament not found");
        }

        if (serverId == null) {
            throw new InvalidDataException("Invalid server");
        }
        Server server = serverRepository.findById(serverId).orElse(null);
        if (server == null) {
            throw new InvalidDataException("Server not found");
        }

        if (gameMapId == null) {
            throw new InvalidDataException("Invalid gamemap");
        }
        GameMap gameMap = gameMapRepository.findById(gameMapId).orElse(null);

        if (gametypeId == null) {
            throw new InvalidDataException("Invalid gametype");
        }
        Gametype gametype = gametypeRepository.findById(gametypeId).orElse(null);

        if (localTeamId == null) {
            throw new InvalidDataException("Invalid local team");
        }
        Team localTeam = teamRepository.findById(localTeamId).orElse(null);

        if (guestTeamId == null) {
            throw new InvalidDataException("Invalid guest team");
        }
        Team guestTeam = teamRepository.findById(guestTeamId).orElse(null);

        Match match = new Match(name, tournament, server, gameMap, gametype, localTeam, guestTeam);
        match = matchRepository.save(match);
        return match;
    }

    @Transactional(readOnly = true)
    public Match read(Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public List<Match> readAll() {
        return matchRepository.findAll();
    }

    @Transactional
    public Match update(Long id, String name, Long tournamentId, Long serverId, Long gameMapId, Long gametypeId, Long localTeamId, Long guestTeamId) {
        Match match = findById(id);

        boolean changed = false;

        if (name != null) {
            match.setName(name);
            changed = true;
        }

        if (tournamentId != null) {
            Tournament tournament = tournamentRepository.findById(tournamentId).orElse(null);
            if (tournament == null) {
                throw new InvalidDataException("Invalid tournament");
            }
            match.setTournament(tournament);
            changed = true;
        }

        if (serverId != null) {
            Server server = serverRepository.findById(serverId).orElse(null);
            if (server == null) {
                throw new InvalidDataException("Invalid server");
            }
            match.setServer(server);
            changed = true;
        }

        if (gameMapId != null) {
            GameMap gameMap = gameMapRepository.findById(gameMapId).orElse(null);
            if (gameMap == null) {
                throw new InvalidDataException("Invalid gameMap");
            }
            match.setGameMap(gameMap);
            changed = true;
        } else if (match.getGameMap() != null) {
            match.setGameMap(null);
            changed = true;
        }

        if (gametypeId != null) {
            Gametype gametype = gametypeRepository.findById(gametypeId).orElse(null);
            if (gametype == null) {
                throw new InvalidDataException("Invalid gametype");
            }
            match.setGametype(gametype);
            changed = true;
        } else if (match.getGametype() != null) {
            match.setGametype(null);
            changed = true;
        }

        if (localTeamId != null) {
            Team localTeam = teamRepository.findById(localTeamId).orElse(null);
            if (localTeam == null) {
                throw new InvalidDataException("Invalid localTeam");
            }
            match.setLocalTeam(localTeam);
            changed = true;
        } else if (match.getLocalTeam() != null) {
            match.setLocalTeam(null);
            changed = true;
        }

        if (guestTeamId != null) {
            Team guestTeam = teamRepository.findById(guestTeamId).orElse(null);
            if (guestTeam == null) {
                throw new InvalidDataException("Invalid guestTeam");
            }
            match.setGuestTeam(guestTeam);
            changed = true;
        } else if (match.getGuestTeam() != null) {
            match.setGuestTeam(null);
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
