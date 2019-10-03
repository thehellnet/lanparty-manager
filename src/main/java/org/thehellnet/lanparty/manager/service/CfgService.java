package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand;
import org.thehellnet.lanparty.manager.model.persistence.*;
import org.thehellnet.lanparty.manager.repository.CfgRepository;
import org.thehellnet.lanparty.manager.repository.GameRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.SeatRepository;
import org.thehellnet.lanparty.manager.utility.cfg.CfgUtility;
import org.thehellnet.lanparty.manager.utility.cfg.ParsedCfg;
import org.thehellnet.utility.StringUtility;

import java.util.List;

@Service
public class CfgService extends AbstractService {

    private static final Logger logger = LoggerFactory.getLogger(CfgService.class);

    private final SeatRepository seatRepository;
    private final PlayerRepository playerRepository;
    private final CfgRepository cfgRepository;
    private final GameRepository gameRepository;

    public CfgService(SeatRepository seatRepository, PlayerRepository playerRepository, CfgRepository cfgRepository, GameRepository gameRepository) {
        this.seatRepository = seatRepository;
        this.playerRepository = playerRepository;
        this.cfgRepository = cfgRepository;
        this.gameRepository = gameRepository;
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

        Seat seat = findSeatByIpAddress(remoteAddress);
        Player player = findPlayerByBarcode(barcode);

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
    public void saveCfg(String remoteAddress, String barcode, List<String> newCfg) {
        if (remoteAddress == null || remoteAddress.length() == 0
                || barcode == null || barcode.length() == 0
                || newCfg == null) {
            throw new InvalidDataException("Invalid remote address or barcode");
        }

        Seat seat = findSeatByIpAddress(remoteAddress);
        Player player = findPlayerByBarcode(barcode);

        Tournament tournament = seat.getTournament();

        Cfg cfg = cfgRepository.findByPlayerAndGame(player, tournament.getGame());
        if (cfg == null) {
            cfg = new Cfg(player, tournament.getGame());
        }

        cfg.setCfg(StringUtility.joinLines(newCfg));
        cfgRepository.save(cfg);
    }

    @Transactional
    public Cfg update(Long playerId, Long gameId, String newCfg) {
        if (playerId == null || gameId == null) {
            throw new InvalidDataException("Invalid playerId or gameId");
        }

        Player player = findPlayerById(playerId);
        Game game = findGameById(gameId);

        Cfg cfg = cfgRepository.findByPlayerAndGame(player, game);
        if (cfg == null) {
            cfg = new Cfg(player, game);
        }

        cfg.setCfg(newCfg);
        cfg = cfgRepository.save(cfg);

        return cfg;
    }

    private Player findPlayerById(Long id) {
        Player player = playerRepository.findById(id).orElse(null);
        if (player == null) {
            throw new NotFoundException();
        }
        return player;
    }

    private Player findPlayerByBarcode(String barcode) {
        Player player = playerRepository.findByBarcode(barcode);
        if (player == null) {
            throw new NotFoundException();
        }
        return player;
    }

    private Seat findSeatByIpAddress(String remoteAddress) {
        Seat seat = seatRepository.findByIpAddress(remoteAddress);
        if (seat == null) {
            throw new NotFoundException();
        }
        return seat;
    }

    private Game findGameById(Long id) {
        Game game = gameRepository.findById(id).orElse(null);
        if (game == null) {
            throw new NotFoundException();
        }
        return game;
    }
}
