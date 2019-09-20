package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.cfg.InvalidInputDataCfgException;
import org.thehellnet.lanparty.manager.exception.game.GameNotFoundException;
import org.thehellnet.lanparty.manager.exception.player.InvalidNamePlayerException;
import org.thehellnet.lanparty.manager.exception.player.PlayerNotFoundException;
import org.thehellnet.lanparty.manager.exception.seat.SeatNotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.*;
import org.thehellnet.lanparty.manager.repository.CfgRepository;
import org.thehellnet.lanparty.manager.repository.GameRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.utility.cfg.CfgUtility;
import org.thehellnet.lanparty.manager.utility.cfg.ParsedCfg;
import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand;
import org.thehellnet.utility.StringUtility;

import java.util.List;

@Service
public class CfgService {

    private static final Logger logger = LoggerFactory.getLogger(CfgService.class);

    private final SeatService seatService;

    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final CfgRepository cfgRepository;

    public CfgService(SeatService seatService, PlayerRepository playerRepository, GameRepository gameRepository, CfgRepository cfgRepository) {
        this.seatService = seatService;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.cfgRepository = cfgRepository;
    }

    @Transactional(readOnly = true)
    public List<String> computeCfg(String remoteAddress, String barcode) throws InvalidInputDataCfgException, SeatNotFoundException, PlayerNotFoundException, InvalidNamePlayerException {
        if (remoteAddress == null || remoteAddress.length() == 0
                || barcode == null || barcode.length() == 0) {
            throw new InvalidInputDataCfgException("Invalid remote address or barcode");
        }

        Seat seat = seatService.findByAddress(remoteAddress);
        if (seat == null) {
            throw new SeatNotFoundException();
        }

        Player player = playerRepository.findByBarcode(barcode);
        if (player == null) {
            throw new PlayerNotFoundException();
        }

        Tournament tournament = seat.getTournament();
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
    public void saveCfg(String remoteAddress, String barcode, List<String> newCfg) throws InvalidInputDataCfgException, SeatNotFoundException, PlayerNotFoundException {
        if (remoteAddress == null || remoteAddress.length() == 0
                || barcode == null || barcode.length() == 0
                || newCfg == null) {
            throw new InvalidInputDataCfgException("Invalid remote address or barcode");
        }

        Seat seat = seatService.findByAddress(remoteAddress);
        if (seat == null) {
            throw new SeatNotFoundException();
        }

        Player player = playerRepository.findByBarcode(barcode);
        if (player == null) {
            throw new PlayerNotFoundException();
        }

        Tournament tournament = seat.getTournament();

        Cfg cfg = cfgRepository.findByPlayerAndGame(player, tournament.getGame());
        if (cfg == null) {
            cfg = new Cfg(player, tournament.getGame());
        }

        cfg.setCfg(StringUtility.joinLines(newCfg));
        cfgRepository.save(cfg);
    }

    @Transactional
    public Cfg save(Long playerId, Long gameId, String newCfg) throws InvalidInputDataCfgException, PlayerNotFoundException, GameNotFoundException {
        if (playerId == null || gameId == null) {
            throw new InvalidInputDataCfgException("Invalid playerId or gameId");
        }

        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            throw new PlayerNotFoundException();
        }

        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            throw new GameNotFoundException();
        }

        Cfg cfg = cfgRepository.findByPlayerAndGame(player, game);
        if (cfg == null) {
            cfg = new Cfg(player, game);
        }

        cfg.setCfg(newCfg);
        cfg = cfgRepository.save(cfg);

        return cfg;
    }
}
