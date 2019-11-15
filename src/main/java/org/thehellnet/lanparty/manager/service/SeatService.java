package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thehellnet.lanparty.manager.model.persistence.Seat;
import org.thehellnet.lanparty.manager.repository.SeatRepository;

@Service
public class SeatService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(SeatService.class);

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public void updateLastContact(String address) {
        Seat seat = seatRepository.findByIpAddress(address);

        logger.info("Updating last contact for seat {}", seat.getName());

        seat.updateLastContact();
        seatRepository.save(seat);
    }
}
