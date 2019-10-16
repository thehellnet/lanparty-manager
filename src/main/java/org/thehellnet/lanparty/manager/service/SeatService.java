package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.persistence.Player;
import org.thehellnet.lanparty.manager.model.persistence.Seat;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.SeatRepository;
import org.thehellnet.lanparty.manager.repository.TournamentRepository;

import java.util.List;

@Service
public class SeatService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(SeatService.class);

    private final SeatRepository seatRepository;
    private final TournamentRepository tournamentRepository;
    private final PlayerRepository playerRepository;

    public SeatService(SeatRepository seatRepository, TournamentRepository tournamentRepository, PlayerRepository playerRepository) {
        this.seatRepository = seatRepository;
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public Seat create(String name, String ipAddress, Long tournamentId, Long playerId) {
        if (name == null) {
            throw new InvalidDataException("Invalid name");
        }

        if (ipAddress == null) {
            throw new InvalidDataException("Invalid ipAddress");
        }

        Tournament tournament = tournamentRepository.findById(tournamentId).orElse(null);
        if (tournament == null) {
            throw new InvalidDataException("Invalid tournament");
        }

        Player player = playerRepository.findById(playerId).orElse(null);

        Seat seat = new Seat(name, ipAddress, tournament, player);
        seat = seatRepository.save(seat);
        return seat;
    }

    @Transactional(readOnly = true)
    public Seat get(Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public List<Seat> getAll() {
        return seatRepository.findAll();
    }

    @Transactional
    public Seat update(Long id, String name, String ipAddress, Long tournamentId, Long playerId) {
        Seat seat = findById(id);

        boolean changed = false;

        if (name != null) {
            seat.setName(name);
            changed = true;
        }

        if (ipAddress != null) {
            seat.setIpAddress(ipAddress);
            changed = true;
        }

        if (tournamentId != null) {
            Tournament tournament = tournamentRepository.findById(tournamentId).orElse(null);
            if (tournament == null) {
                throw new InvalidDataException("Invalid tournament");
            }
            seat.setTournament(tournament);
            changed = true;
        }

        if (playerId != null) {
            Player player = playerRepository.findById(playerId).orElse(null);
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

        return seatRepository.save(seat);
    }

    @Transactional
    public void delete(Long id) {
        Seat seat = findById(id);
        seatRepository.delete(seat);
    }

    @Transactional(readOnly = true)
    public Seat findById(Long id) {
        Seat seat = seatRepository.findById(id).orElse(null);
        if (seat == null) {
            throw new NotFoundException();
        }
        return seat;
    }

    @Transactional(readOnly = true)
    public Seat findByAddress(String address) {
        Seat seat = seatRepository.findByIpAddress(address);
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
        seatRepository.save(seat);
    }
}
