package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.persistence.Team;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.TeamRepository;
import org.thehellnet.lanparty.manager.repository.TournamentRepository;

import java.util.List;

@Service
public class TeamService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, TournamentRepository tournamentRepository) {
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional
    public Team create(String name, Long tournamentId) {
        if (name == null) {
            throw new InvalidDataException("Invalid name");
        }

        Tournament tournament = tournamentRepository.findById(tournamentId).orElse(null);
        if (tournament == null) {
            throw new InvalidDataException("Invalid tournament");
        }

        Team team = new Team(name, tournament);
        team = teamRepository.save(team);
        return team;
    }

    @Transactional(readOnly = true)
    public Team get(Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public List<Team> getAll() {
        return teamRepository.findAll();
    }

    @Transactional
    public Team update(Long id, String name, Long tournamentId) {
        Team team = findById(id);

        boolean changed = false;

        if (name != null) {
            team.setName(name);
            changed = true;
        }

        if (tournamentId != null) {
            Tournament tournament = tournamentRepository.findById(tournamentId).orElse(null);
            if (tournament == null) {
                throw new InvalidDataException("Invalid tournament");
            }
            team.setTournament(tournament);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return teamRepository.save(team);
    }

    @Transactional
    public void delete(Long id) {
        Team team = findById(id);
        teamRepository.delete(team);
    }

    @Transactional(readOnly = true)
    public Team findById(Long id) {
        Team team = teamRepository.findById(id).orElse(null);
        if (team == null) {
            throw new NotFoundException();
        }
        return team;
    }
}
