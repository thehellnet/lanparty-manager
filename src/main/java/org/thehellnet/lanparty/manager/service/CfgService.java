package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.cfg.InvalidDataCfgException;
import org.thehellnet.lanparty.manager.exception.player.PlayerNotFoundException;
import org.thehellnet.lanparty.manager.exception.seat.SeatNotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.Player;
import org.thehellnet.lanparty.manager.model.persistence.Seat;
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

    @Transactional(readOnly = true)
    public String getCfgFromRemoteAddressAndBarcode(String remoteAddress, String barcode) throws InvalidDataCfgException, SeatNotFoundException, PlayerNotFoundException {
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

        return CfgUtility.sanitize(tournament.getCfg(), player.getCfg());
    }
}
