package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.cfg.CfgNotFoundException;
import org.thehellnet.lanparty.manager.exception.cfg.InvalidDataCfgException;
import org.thehellnet.lanparty.manager.exception.player.PlayerNotFoundException;
import org.thehellnet.lanparty.manager.exception.seat.SeatNotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.*;
import org.thehellnet.lanparty.manager.repository.CfgRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.utility.cfg.CfgUtility;

@Service
public class CfgService {

    private static final Logger logger = LoggerFactory.getLogger(CfgService.class);

    private final SeatService seatService;

    private final PlayerRepository playerRepository;
    private final CfgRepository cfgRepository;

    public CfgService(SeatService seatService, PlayerRepository playerRepository, CfgRepository cfgRepository) {
        this.seatService = seatService;
        this.playerRepository = playerRepository;
        this.cfgRepository = cfgRepository;
    }

    @Transactional(readOnly = true)
    public String getCfgFromRemoteAddressAndBarcode(String remoteAddress, String barcode) throws InvalidDataCfgException, SeatNotFoundException, PlayerNotFoundException, CfgNotFoundException {
        if (remoteAddress == null
                || remoteAddress.length() == 0
                || barcode == null
                || barcode.length() == 0) {
            throw new InvalidDataCfgException("Invalid remote address or barcode");
        }

        Seat seat = seatService.findByAddress(remoteAddress);
        if (seat == null) {
            throw new SeatNotFoundException("Seat not found");
        }

        Player player = playerRepository.findByBarcode(barcode);
        if (player == null) {
            throw new PlayerNotFoundException("Player not found");
        }

        Tournament tournament = seat.getTournament();
        String tournamentCfg = tournament.getCfg();

        String playerCfg = getPlayerCfgInGame(player, tournament.getGame());
        String sanitizedCfg = CfgUtility.sanitize(tournamentCfg, playerCfg);

        return sanitizedCfg;
    }

    @Transactional(readOnly = true)
    public String getPlayerCfgInGame(Player player, Game game) {
        String playerCfg = "";

        Cfg cfg = cfgRepository.findByPlayerAndGame(player, game);
        if (cfg != null) {
            playerCfg = cfg.getCfg();
        }
        return playerCfg;
    }

//    @Transactional
//    public String saveCfg(Player player, String cfg) {
//
//    }
}
