package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.Player;
import org.thehellnet.lanparty.manager.model.persistence.Seat;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.SeatRepository;

import java.util.List;

@Service
public class SeatService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(SeatService.class);

    public SeatService(SeatRepository seatRepository,
                       PlayerRepository playerRepository,
                       AppUserRepository appUserRepository) {
        super(seatRepository, playerRepository, appUserRepository);
    }

    @Transactional
    public void updateLastContact(String address) {
        Seat seat = seatRepository.findByIpAddress(address);
        if (seat == null) {
            throw new NotFoundException();
        }

        logger.info("Updating last contact for seat {}", seat.getName());

        seat.updateLastContact();
        seatRepository.save(seat);
    }

    @Transactional
    public void updatePlayerInSeats(String remoteAddress, String barcode) {
        TokenData tokenData = getTokenData(remoteAddress, barcode);

        Seat seat = tokenData.getSeat();
        Player player = tokenData.getPlayer();

        logger.info("Updating player for seat {}: {}", seat, player);

        List<Seat> playerList = seatRepository.findAllByPlayer(player);
        for (Seat oldSeat : playerList) {
            logger.debug("Removing player {} from old seat {}", player, oldSeat);
            oldSeat.setPlayer(null);
            seatRepository.save(oldSeat);
        }

        logger.debug("Setting player {} in new seat {}", player, seat);

        seat.setPlayer(player);
        seatRepository.save(seat);
    }
}
