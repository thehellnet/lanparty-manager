package org.thehellnet.lanparty.manager.service.crud;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.dto.service.CfgServiceDTO;
import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand;
import org.thehellnet.lanparty.manager.model.persistence.*;
import org.thehellnet.lanparty.manager.repository.*;
import org.thehellnet.lanparty.manager.utility.cfg.CfgUtility;
import org.thehellnet.lanparty.manager.utility.cfg.ParsedCfg;
import org.thehellnet.utility.StringUtility;

import java.util.List;

@Service
public class CfgCrudServiceOLD extends AbstractCrudServiceOLD<Cfg, CfgServiceDTO, CfgRepository> {

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

    private final SeatRepository seatRepository;
    private final AppUserRepository appUserRepository;
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;

    public CfgCrudServiceOLD(CfgRepository repository, SeatRepository seatRepository, AppUserRepository appUserRepository, PlayerRepository playerRepository, GameRepository gameRepository) {
        super(repository);
        this.seatRepository = seatRepository;
        this.appUserRepository = appUserRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    @Transactional
    public Cfg create(CfgServiceDTO dto) {
        if (dto.playerId == null) {
            throw new InvalidDataException("Invalid player");
        }
        Player player = playerRepository.findById(dto.playerId).orElse(null);
        if (player == null) {
            throw new InvalidDataException("Player not found");
        }

        if (dto.gameId == null) {
            throw new InvalidDataException("Invalid game");
        }
        Game game = gameRepository.findById(dto.gameId).orElse(null);
        if (game == null) {
            throw new InvalidDataException("Game not found");
        }

        Cfg cfg = new Cfg(player, game);

        if (dto.cfgContent != null) {
            cfg.setCfgContent(dto.cfgContent);
        }

        cfg = repository.save(cfg);

        return cfg;
    }

    @Override
    @Transactional
    public Cfg update(Long id, CfgServiceDTO dto) {
        Cfg cfg = findById(id);

        boolean changed = false;

        if (dto.playerId != null) {
            Player player = playerRepository.findById(dto.playerId).orElse(null);
            if (player == null) {
                throw new InvalidDataException("Game not found");
            }
            cfg.setPlayer(player);
            changed = true;
        }

        if (dto.gameId != null) {
            Game game = gameRepository.findById(dto.gameId).orElse(null);
            if (game == null) {
                throw new InvalidDataException("Game not found");
            }
            cfg.setGame(game);
            changed = true;
        }

        if (dto.cfgContent != null) {
            cfg.setCfgContent(dto.cfgContent);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return repository.save(cfg);
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
            throw new NotFoundException("AppUser not found");
        }

        Player player = playerRepository.findByAppUserAndTournament(appUser, tournament);
        if (player == null) {
            throw new NotFoundException("Player not found");
        }

        String tournamentCfg = tournament.getCfg();
        List<ParsedCfgCommand> tournamentCfgCommands = CfgUtility.parseCfgFromString(tournamentCfg);
        tournamentCfgCommands = CfgUtility.removeSpecialCommands(tournamentCfgCommands);

        Cfg cfg = repository.findByPlayerAndGame(player, tournament.getGame());
        String playerCfg = cfg != null ? cfg.getCfgContent() : null;

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

        Cfg cfg = repository.findByPlayerAndGame(player, tournament.getGame());
        if (cfg == null) {
            cfg = new Cfg(player, tournament.getGame());
        }

        cfg.setCfgContent(StringUtility.joinLines(newCfg));
        repository.save(cfg);
    }
}
