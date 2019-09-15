package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.team.TeamAlreadyExistsException;
import org.thehellnet.lanparty.manager.exception.team.TeamInvalidNameOrTournamentIDException;
import org.thehellnet.lanparty.manager.exception.tournament.TournamentNotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.Team;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.TeamRepository;
import org.thehellnet.lanparty.manager.repository.TournamentRepository;

@Service
public class TeamService {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, TournamentRepository tournamentRepository) {
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional(readOnly = true)
    public Team findByName(String name) {
        if (name == null || name.length() == 0) {
            return null;
        }

        return teamRepository.findByName(name);
    }

    @Transactional
    public Team create(String name, Long tournamentId) throws TeamInvalidNameOrTournamentIDException, TeamAlreadyExistsException, TournamentNotFoundException {
        if (name == null || name.length() == 0
                || tournamentId == null) {
            throw new TeamInvalidNameOrTournamentIDException();
        }

        Team team = teamRepository.findByName(name);
        if (team != null) {
            throw new TeamAlreadyExistsException();
        }

        Tournament tournament = tournamentRepository.findById(tournamentId).orElse(null);
        if (tournament == null) {
            throw new TournamentNotFoundException();
        }

        team = new Team(name, tournament);
        team = teamRepository.save(team);
        return team;
    }
}
