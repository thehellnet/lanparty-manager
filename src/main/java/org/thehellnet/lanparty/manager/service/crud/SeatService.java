package org.thehellnet.lanparty.manager.service.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.dto.service.SeatServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.Player;
import org.thehellnet.lanparty.manager.model.persistence.Seat;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.SeatRepository;
import org.thehellnet.lanparty.manager.repository.TournamentRepository;

@Service
public class SeatService extends AbstractCrudService<Seat, SeatServiceDTO, SeatRepository> {

    private static final Logger logger = LoggerFactory.getLogger(SeatService.class);

    private final TournamentRepository tournamentRepository;
    private final PlayerRepository playerRepository;

    public SeatService(SeatRepository repository, TournamentRepository tournamentRepository, PlayerRepository playerRepository) {
        super(repository);
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    @Transactional
    public Seat create(SeatServiceDTO dto) {
        if (dto.name == null) {
            throw new InvalidDataException("Invalid name");
        }

        if (dto.ipAddress == null) {
            throw new InvalidDataException("Invalid ipAddress");
        }

        if (dto.tournamentId == null) {
            throw new InvalidDataException("Invalid tournament");
        }
        Tournament tournament = tournamentRepository.findById(dto.tournamentId).orElse(null);
        if (tournament == null) {
            throw new InvalidDataException("Tournament not found");
        }

        if (dto.playerId == null) {
            throw new InvalidDataException("Invalid player");
        }
        Player player = playerRepository.findById(dto.playerId).orElse(null);

        Seat seat = new Seat(dto.name, dto.ipAddress, tournament, player);
        seat = repository.save(seat);
        return seat;
    }

    @Override
    @Transactional
    public Seat update(Long id, SeatServiceDTO dto) {
        Seat seat = findById(id);

        boolean changed = false;

        if (dto.name != null) {
            seat.setName(dto.name);
            changed = true;
        }

        if (dto.ipAddress != null) {
            seat.setIpAddress(dto.ipAddress);
            changed = true;
        }

        if (dto.tournamentId != null) {
            Tournament tournament = tournamentRepository.findById(dto.tournamentId).orElse(null);
            if (tournament == null) {
                throw new InvalidDataException("Invalid tournament");
            }
            seat.setTournament(tournament);
            changed = true;
        }

        if (dto.playerId != null) {
            Player player = playerRepository.findById(dto.playerId).orElse(null);
            if (player == null) {
                throw new InvalidDataException("Invalid player");
            }
            seat.setPlayer(player);
            changed = true;
        } else if (seat.getPlayer() != null) {
            seat.setPlayer(null);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return repository.save(seat);
    }

    @Transactional(readOnly = true)
    public Seat findByAddress(String address) {
        Seat seat = repository.findByIpAddress(address);
        if (seat == null) {
            throw new NotFoundException("Seat not found");
        }

        return seat;
    }

    @Transactional
    public void updateLastContact(String address) {
        Seat seat = findByAddress(address);

        logger.info("Updating last contact for seat {}", seat.getName());

        seat.updateLastContact();
        repository.save(seat);
    }
}
