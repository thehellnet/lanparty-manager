package org.thehellnet.lanparty.manager.service.crud;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.dto.service.TeamServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.Team;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.TeamRepository;
import org.thehellnet.lanparty.manager.repository.TournamentRepository;

@Service
public class TeamCrudServiceOLD extends AbstractCrudServiceOLD<Team, TeamServiceDTO, TeamRepository> {

    private final TournamentRepository tournamentRepository;

    public TeamCrudServiceOLD(TeamRepository repository, TournamentRepository tournamentRepository) {
        super(repository);
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    @Transactional
    public Team create(TeamServiceDTO dto) {
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

        Team team = new Team(dto.name, tournament);
        team = repository.save(team);
        return team;
    }

    @Override
    @Transactional
    public Team update(Long id, TeamServiceDTO dto) {
        Team team = findById(id);

        boolean changed = false;

        if (dto.name != null) {
            team.setName(dto.name);
            changed = true;
        }

        if (dto.tournamentId != null) {
            Tournament tournament = tournamentRepository.findById(dto.tournamentId).orElse(null);
            if (tournament == null) {
                throw new InvalidDataException("Invalid tournament");
            }
            team.setTournament(tournament);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return repository.save(team);
    }
}
