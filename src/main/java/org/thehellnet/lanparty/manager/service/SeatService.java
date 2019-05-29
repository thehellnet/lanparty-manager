package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.persistence.Seat;
import org.thehellnet.lanparty.manager.repository.SeatRepository;

@Service
public class SeatService {

    private static final Logger logger = LoggerFactory.getLogger(SeatService.class);

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
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
}
