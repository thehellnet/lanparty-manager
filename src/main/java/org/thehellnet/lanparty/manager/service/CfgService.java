package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.persistence.Player;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.CfgRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.TournamentRepository;
import org.thehellnet.utility.StringUtility;
import org.thehellnet.utility.cfg.CfgUtility;

@Service
public class CfgService {

    private static final Logger logger = LoggerFactory.getLogger(CfgService.class);

    private final SeatService seatService;

    private final PlayerRepository playerRepository;
    private final TournamentRepository tournamentRepository;
    private final CfgRepository cfgRepository;

    public CfgService(SeatService seatService, PlayerRepository playerRepository, TournamentRepository tournamentRepository, CfgRepository cfgRepository) {
        this.seatService = seatService;
        this.playerRepository = playerRepository;
        this.tournamentRepository = tournamentRepository;
        this.cfgRepository = cfgRepository;
    }

    @Transactional(readOnly = true)
    public String[] getPlayerCfgInTournament(Tournament tournament, Player player) {
        if (tournament == null
                || player == null) {
            return null;
        }

        tournament = tournamentRepository.getOne(tournament.getId());
        player = playerRepository.getOne(player.getId());

        String tournamentCfg = tournament.getCfg();
        String playerCfg = player.getCfg();

        String cfg = CfgUtility.sanitize(tournamentCfg, playerCfg);
        return StringUtility.splitLines(cfg).toArray(new String[0]);
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
