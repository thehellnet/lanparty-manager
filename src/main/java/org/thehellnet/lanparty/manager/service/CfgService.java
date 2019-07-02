package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.cfg.CfgException;
import org.thehellnet.lanparty.manager.model.persistence.Cfg;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.Player;
import org.thehellnet.lanparty.manager.model.persistence.Seat;
import org.thehellnet.lanparty.manager.repository.CfgRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;

@Service
public class CfgService {

    private static final Logger logger = LoggerFactory.getLogger(CfgService.class);

    private final PlayerRepository playerRepository;
    private final SeatService seatService;
    private final CfgRepository cfgRepository;

    public CfgService(PlayerRepository playerRepository, SeatService seatService, CfgRepository cfgRepository) {
        this.playerRepository = playerRepository;
        this.seatService = seatService;
        this.cfgRepository = cfgRepository;
    }

//    @Transactional(readOnly = true)
//    public String getCfgContentFromRemoteAddressAndBarcode(String remoteAddress, String barcode) throws CfgException {
//        if (remoteAddress == null
//                || remoteAddress.length() == 0
//                || barcode == null
//                || barcode.length() == 0) {
//            throw new CfgException("Invalid remote address or barcode");
//        }
//
//        Seat seat = seatService.findByAddress(remoteAddress);
//        if (seat == null) {
//            throw new CfgException("Seat not found");
//        }
//
//        Player player = playerRepository.findByBarcode(barcode);
//        if (player == null) {
//            throw new CfgException("Player not found");
//        }
//
//        Game game = seat.getTournament().getGame();
//
//        Cfg playerCfg = cfgRepository.findByPlayerAndGame(player, game);
//        if (playerCfg == null) {
//            throw new CfgException("Cfg not found");
//        }
//
//        return playerCfg.getCfg();
//    }
}
