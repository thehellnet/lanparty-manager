package org.thehellnet.lanparty.manager.service.crud;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.dto.service.MatchServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.*;
import org.thehellnet.lanparty.manager.repository.*;

@Service
public class MatchCrudService extends AbstractCrudService<Match, MatchServiceDTO, MatchRepository> {

    private final TournamentRepository tournamentRepository;
    private final ServerRepository serverRepository;
    private final GameMapRepository gameMapRepository;
    private final GametypeRepository gametypeRepository;
    private final TeamRepository teamRepository;

    public MatchCrudService(MatchRepository repository, TournamentRepository tournamentRepository, ServerRepository serverRepository, GameMapRepository gameMapRepository, GametypeRepository gametypeRepository, TeamRepository teamRepository) {
        super(repository);
        this.tournamentRepository = tournamentRepository;
        this.serverRepository = serverRepository;
        this.gameMapRepository = gameMapRepository;
        this.gametypeRepository = gametypeRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    @Transactional
    public Match create(MatchServiceDTO dto) {
        if (dto.name == null) {
            throw new InvalidDataException("Invalid name");
        }

        if (dto.tournamentId == null) {
            throw new InvalidDataException("Invalid tournament");
        }
        Tournament tournament = tournamentRepository.findById(dto.tournamentId).orElse(null);
        if (tournament == null) {
            throw new InvalidDataException("Tournament not found");
        }

        if (dto.serverId == null) {
            throw new InvalidDataException("Invalid server");
        }
        Server server = serverRepository.findById(dto.serverId).orElse(null);
        if (server == null) {
            throw new InvalidDataException("Server not found");
        }

        if (dto.gameMapId == null) {
            throw new InvalidDataException("Invalid gamemap");
        }
        GameMap gameMap = gameMapRepository.findById(dto.gameMapId).orElse(null);

        if (dto.gametypeId == null) {
            throw new InvalidDataException("Invalid gametype");
        }
        Gametype gametype = gametypeRepository.findById(dto.gametypeId).orElse(null);

        if (dto.localTeamId == null) {
            throw new InvalidDataException("Invalid local team");
        }
        Team localTeam = teamRepository.findById(dto.localTeamId).orElse(null);

        if (dto.guestTeamId == null) {
            throw new InvalidDataException("Invalid guest team");
        }
        Team guestTeam = teamRepository.findById(dto.guestTeamId).orElse(null);

        Match match = new Match(dto.name, tournament, server, gameMap, gametype, localTeam, guestTeam);
        match = repository.save(match);
        return match;
    }

    @Override
    @Transactional
    public Match update(Long id, MatchServiceDTO dto) {
        Match match = findById(id);

        boolean changed = false;

        if (dto.name != null) {
            match.setName(dto.name);
            changed = true;
        }

        if (dto.tournamentId != null) {
            Tournament tournament = tournamentRepository.findById(dto.tournamentId).orElse(null);
            if (tournament == null) {
                throw new InvalidDataException("Invalid tournament");
            }
            match.setTournament(tournament);
            changed = true;
        }

        if (dto.serverId != null) {
            Server server = serverRepository.findById(dto.serverId).orElse(null);
            if (server == null) {
                throw new InvalidDataException("Invalid server");
            }
            match.setServer(server);
            changed = true;
        }

        if (dto.gameMapId != null) {
            GameMap gameMap = gameMapRepository.findById(dto.gameMapId).orElse(null);
            if (gameMap == null) {
                throw new InvalidDataException("Invalid gameMap");
            }
            match.setGameMap(gameMap);
            changed = true;
        } else if (match.getGameMap() != null) {
            match.setGameMap(null);
            changed = true;
        }

        if (dto.gametypeId != null) {
            Gametype gametype = gametypeRepository.findById(dto.gametypeId).orElse(null);
            if (gametype == null) {
                throw new InvalidDataException("Invalid gametype");
            }
            match.setGametype(gametype);
            changed = true;
        } else if (match.getGametype() != null) {
            match.setGametype(null);
            changed = true;
        }

        if (dto.localTeamId != null) {
            Team localTeam = teamRepository.findById(dto.localTeamId).orElse(null);
            if (localTeam == null) {
                throw new InvalidDataException("Invalid localTeam");
            }
            match.setLocalTeam(localTeam);
            changed = true;
        } else if (match.getLocalTeam() != null) {
            match.setLocalTeam(null);
            changed = true;
        }

        if (dto.guestTeamId != null) {
            Team guestTeam = teamRepository.findById(dto.guestTeamId).orElse(null);
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

        return repository.save(match);
    }
}
