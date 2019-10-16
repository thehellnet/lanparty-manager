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

import java.util.List;

@Service
public class TeamService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Transactional
    public Team create(String name, Tournament tournament) {
        if (name == null) {
            throw new InvalidDataException("Invalid name");
        }
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
    public Team update(Long id, String name, Tournament tournament) {
        Team team = findById(id);

        boolean changed = false;

        if (name != null) {
            team.setName(name);
            changed = true;
        }

        if (tournament != null) {
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
