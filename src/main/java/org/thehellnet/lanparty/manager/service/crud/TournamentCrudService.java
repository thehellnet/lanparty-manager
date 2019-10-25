package org.thehellnet.lanparty.manager.service.crud;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.dto.service.TournamentServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.GameRepository;
import org.thehellnet.lanparty.manager.repository.TournamentRepository;

@Service
public class TournamentCrudService extends AbstractCrudService<Tournament, TournamentServiceDTO, TournamentRepository> {

    private final GameRepository gameRepository;

    public TournamentCrudService(TournamentRepository repository, GameRepository gameRepository) {
        super(repository);
        this.gameRepository = gameRepository;
    }

    @Override
    @Transactional
    public Tournament create(TournamentServiceDTO dto) {
        if (dto.name == null) {
            throw new InvalidDataException("Invalid name");
        }

        if (dto.gameId == null) {
            throw new InvalidDataException("Invalid game");
        }
        Game game = gameRepository.findById(dto.gameId).orElse(null);
        if (game == null) {
            throw new InvalidDataException("Game not found");
        }

        if (dto.cfg == null) {
            throw new InvalidDataException("Invalid cfg");
        }

        Tournament tournament = new Tournament(dto.name, game, dto.cfg);
        tournament = repository.save(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament update(Long id, TournamentServiceDTO dto) {
        Tournament tournament = findById(id);

        boolean changed = false;

        if (dto.name != null) {
            tournament.setName(dto.name);
            changed = true;
        }

        if (dto.gameId != null) {
            Game game = gameRepository.findById(dto.gameId).orElse(null);
            if (game == null) {
                throw new InvalidDataException("Invalid game");
            }

            tournament.setGame(game);
            changed = true;
        }

        if (dto.cfg != null) {
            tournament.setCfg(dto.cfg);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return repository.save(tournament);
    }
}
