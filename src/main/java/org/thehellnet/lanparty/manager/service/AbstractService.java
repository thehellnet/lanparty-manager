package org.thehellnet.lanparty.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Player;
import org.thehellnet.lanparty.manager.model.persistence.Seat;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.SeatRepository;

import java.util.List;

public abstract class AbstractService {

    protected static class TokenData {
        private final Seat seat;
        private final Tournament tournament;
        private final AppUser appUser;
        private final Player player;

        public TokenData(Seat seat, Tournament tournament, AppUser appUser, Player player) {
            this.seat = seat;
            this.tournament = tournament;
            this.appUser = appUser;
            this.player = player;
        }

        public Seat getSeat() {
            return seat;
        }

        public Tournament getTournament() {
            return tournament;
        }

        public AppUser getAppUser() {
            return appUser;
        }

        public Player getPlayer() {
            return player;
        }
    }

    protected final SeatRepository seatRepository;
    protected final PlayerRepository playerRepository;
    protected final AppUserRepository appUserRepository;

    @Autowired
    protected AbstractService(SeatRepository seatRepository,
                              PlayerRepository playerRepository,
                              AppUserRepository appUserRepository) {
        this.seatRepository = seatRepository;
        this.playerRepository = playerRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional(readOnly = true)
    protected TokenData getTokenData(String remoteAddress, String barcode) {
        Seat seat = seatRepository.findByIpAddress(remoteAddress);
        if (seat == null) {
            throw new NotFoundException("Seat not found");
        }

        Tournament tournament = seat.getTournament();
        if (tournament == null) {
            throw new NotFoundException("Tournament not found");
        }

        AppUser appUser = appUserRepository.findByBarcode(barcode);
        if (appUser == null) {
            throw new NotFoundException("AppUser not found");
        }

        Player player = playerRepository.findByAppUserAndTournament(appUser, tournament);
        if (player == null) {
            throw new NotFoundException("Player not found");
        }

        return new TokenData(seat, tournament, appUser, player);
    }

    protected static String[] parseStringList(Object object) {
        return parseStringList(object, true);
    }

    protected static String[] parseStringList(Object object, boolean required) {
        if (object != null) {
            if (object instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> stringList = (List<String>) object;
                return stringList.toArray(new String[0]);
            }
        }

        if (required) {
            throw new InvalidDataException("Invalid string list");
        }

        return null;
    }
}
