package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.seat.SeatAlreadyExistsException;
import org.thehellnet.lanparty.manager.exception.seat.SeatInvalidNameException;
import org.thehellnet.lanparty.manager.exception.tournament.TournamentNotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.Seat;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.SeatRepository;
import org.thehellnet.lanparty.manager.repository.TournamentRepository;

@Service
public class SeatService {

    private static final Logger logger = LoggerFactory.getLogger(SeatService.class);

    private final SeatRepository seatRepository;
    private final TournamentRepository tournamentRepository;

    public SeatService(SeatRepository seatRepository, TournamentRepository tournamentRepository) {
        this.seatRepository = seatRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional(readOnly = true)
    public Seat findByAddress(String address) {
        if (address == null || address.length() == 0) {
            return null;
        }

        return seatRepository.findByIpAddress(address);
    }

    @Transactional
    public void updateLastContact(Seat seat) {
        seat = seatRepository.findById(seat.getId()).orElse(null);
        if (seat == null) {
            return;
        }

        logger.info("Updating last contact for seat {}", seat.getName());

        seat.updateLastContact();
        seatRepository.save(seat);
    }

    @Transactional
    public Seat create(String name, String ipAddress, Long tournamentId) throws SeatInvalidNameException, TournamentNotFoundException, SeatAlreadyExistsException {
        if (name == null || name.length() == 0) {
            throw new SeatInvalidNameException();
        }

        Tournament tournament = tournamentRepository.findById(tournamentId).orElse(null);
        if (tournament == null) {
            throw new TournamentNotFoundException();
        }

        Seat seat = seatRepository.findByIpAddress(ipAddress);
        if (seat != null) {
            throw new SeatAlreadyExistsException();
        }

        seat = new Seat(name, ipAddress, tournament);
        seat = seatRepository.save(seat);
        return seat;
    }
}
