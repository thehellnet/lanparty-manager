package org.thehellnet.lanparty.manager.service.crud;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.dto.service.ShowcaseServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.Match;
import org.thehellnet.lanparty.manager.model.persistence.Showcase;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.MatchRepository;
import org.thehellnet.lanparty.manager.repository.ShowcaseRepository;
import org.thehellnet.lanparty.manager.repository.TournamentRepository;

@Service
public class ShowcaseCrudService extends AbstractCrudService<Showcase, ShowcaseServiceDTO, ShowcaseRepository> {

    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;

    public ShowcaseCrudService(ShowcaseRepository repository, TournamentRepository tournamentRepository, MatchRepository matchRepository) {
        super(repository);
        this.tournamentRepository = tournamentRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    @Transactional
    public Showcase create(ShowcaseServiceDTO dto) {
        if (dto.name == null) {
            throw new InvalidDataException("Invalid name");
        }

        if (dto.mode == null) {
            throw new InvalidDataException("Invalid mode");
        }

        Tournament tournament = tournamentRepository.findById(dto.tournamentId).orElse(null);
        Match match = matchRepository.findById(dto.matchId).orElse(null);

        Showcase showcase = new Showcase(dto.tag, dto.name, dto.mode, tournament, match, dto.lastAddress, dto.lastContact);
        showcase = repository.save(showcase);
        return showcase;
    }

    @Override
    @Transactional
    public Showcase update(Long id, ShowcaseServiceDTO dto) {
        Showcase showcase = findById(id);

        boolean changed = false;

        if (dto.tag != null) {
            showcase.setTag(dto.tag);
            changed = true;
        }

        if (dto.name != null) {
            showcase.setName(dto.name);
            changed = true;
        }

        if (dto.mode != null) {
            showcase.setMode(dto.mode);
            changed = true;
        }

        if (dto.tournamentId != null) {
            Tournament tournament = tournamentRepository.findById(dto.tournamentId).orElse(null);
            if (tournament == null) {
                throw new InvalidDataException("Invalid tournament");
            }
            showcase.setTournament(tournament);
            changed = true;
        }

        if (dto.matchId != null) {
            Match match = matchRepository.findById(dto.matchId).orElse(null);
            if (match == null) {
                throw new InvalidDataException("Invalid match");
            }
            showcase.setMatch(match);
            changed = true;
        }

        if (dto.lastAddress != null) {
            showcase.setLastAddress(dto.lastAddress);
            changed = true;
        }

        if (dto.lastContact != null) {
            showcase.setLastContact(dto.lastContact);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return repository.save(showcase);
    }
}
