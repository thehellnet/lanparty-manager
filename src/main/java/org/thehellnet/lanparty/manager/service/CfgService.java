package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand;
import org.thehellnet.lanparty.manager.model.persistence.*;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.CfgRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.SeatRepository;
import org.thehellnet.lanparty.manager.utility.cfg.CfgUtility;
import org.thehellnet.lanparty.manager.utility.cfg.ParsedCfg;
import org.thehellnet.utility.StringUtility;

import java.util.List;

@Service
public class CfgService extends AbstractService {

    private class FindTournamentAndPlayer {
        private String remoteAddress;
        private String barcode;
        private Tournament tournament;
        private Player player;

        public FindTournamentAndPlayer(String remoteAddress, String barcode) {
            this.remoteAddress = remoteAddress;
            this.barcode = barcode;
        }

        public Tournament getTournament() {
            return tournament;
        }

        public Player getPlayer() {
            return player;
        }

        public FindTournamentAndPlayer invoke() {
            Seat seat = seatRepository.findByIpAddress(remoteAddress);
            if (seat == null) {
                throw new NotFoundException("Seat not found");
            }

            tournament = seat.getTournament();
            if (tournament == null) {
                throw new NotFoundException("Tournament not found");
            }

            AppUser appUser = appUserRepository.findByBarcode(barcode);
            if (appUser == null) {
                throw new NotFoundException("AppUser not found");
            }

            player = playerRepository.findByAppUserAndTournament(appUser, tournament);
            if (player == null) {
                throw new NotFoundException("Player not found");
            }

            return this;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(CfgService.class);

    private final SeatRepository seatRepository;
    private final AppUserRepository appUserRepository;
    private final PlayerRepository playerRepository;
    private final CfgRepository cfgRepository;

    @Autowired
    public CfgService(SeatRepository seatRepository, AppUserRepository appUserRepository, PlayerRepository playerRepository, CfgRepository cfgRepository) {
        this.seatRepository = seatRepository;
        this.appUserRepository = appUserRepository;
        this.playerRepository = playerRepository;
        this.cfgRepository = cfgRepository;
    }

    @Transactional(readOnly = true)
    public Cfg findByPlayerAndGame(Player player, Game game) {
        Cfg cfg = cfgRepository.findByPlayerAndGame(player, game);
        if (cfg == null) {
            throw new NotFoundException();
        }

        return cfg;
    }

    @Transactional(readOnly = true)
    public List<String> computeCfg(String remoteAddress, String barcode) {
        if (remoteAddress == null || remoteAddress.length() == 0
                || barcode == null || barcode.length() == 0) {
            throw new InvalidDataException("Invalid remote address or barcode");
        }

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
            throw new NotFoundException("Player not found");
        }

        Player player = playerRepository.findByAppUserAndTournament(appUser, tournament);
        if (player == null) {
            throw new NotFoundException("Player not found");
        }

        String tournamentCfg = tournament.getCfg();
        List<ParsedCfgCommand> tournamentCfgCommands = CfgUtility.parseCfgFromString(tournamentCfg);
        tournamentCfgCommands = CfgUtility.removeSpecialCommands(tournamentCfgCommands);

        Cfg cfg = cfgRepository.findByPlayerAndGame(player, tournament.getGame());
        String playerCfg = cfg != null ? cfg.getCfg() : null;

        List<ParsedCfgCommand> playerCfgCommands = CfgUtility.parseCfgFromString(playerCfg);
        playerCfgCommands = CfgUtility.removeSpecialCommands(playerCfgCommands);

        List<ParsedCfgCommand> commands = CfgUtility.mergeTournamentWithPlayer(tournamentCfgCommands, playerCfgCommands);

        ParsedCfg parsedCfg = new ParsedCfg(commands, player.getNickname());
        return parsedCfg.toStringList();
    }

    @Transactional
    public void saveCfg(String remoteAddress, String barcode, List<String> newCfg) {
        if (remoteAddress == null || remoteAddress.length() == 0
                || barcode == null || barcode.length() == 0
                || newCfg == null) {
            throw new InvalidDataException("Invalid remote address or barcode");
        }

        FindTournamentAndPlayer findTournamentAndPlayer = new FindTournamentAndPlayer(remoteAddress, barcode).invoke();
        Tournament tournament = findTournamentAndPlayer.getTournament();
        Player player = findTournamentAndPlayer.getPlayer();

        Cfg cfg = cfgRepository.findByPlayerAndGame(player, tournament.getGame());
        if (cfg == null) {
            cfg = new Cfg(player, tournament.getGame());
        }

        cfg.setCfg(StringUtility.joinLines(newCfg));
        cfgRepository.save(cfg);
    }
}
