package org.thehellnet.lanparty.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.InvalidDataException;
import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand;
import org.thehellnet.lanparty.manager.model.persistence.Cfg;
import org.thehellnet.lanparty.manager.model.persistence.Player;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.CfgRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.SeatRepository;
import org.thehellnet.lanparty.manager.utility.cfg.ParsedCfgCommandMerger;
import org.thehellnet.lanparty.manager.utility.cfg.ParsedCfgCommandParser;
import org.thehellnet.lanparty.manager.utility.cfg.ParsedCfgCommandSanitizer;
import org.thehellnet.utility.StringUtility;

import java.util.List;

@Service
public class CfgService extends AbstractService {

    private final CfgRepository cfgRepository;

    @Autowired
    public CfgService(SeatRepository seatRepository,
                      AppUserRepository appUserRepository,
                      PlayerRepository playerRepository,
                      CfgRepository cfgRepository) {
        super(seatRepository, playerRepository, appUserRepository);
        this.cfgRepository = cfgRepository;
    }

    @Transactional(readOnly = true)
    public List<ParsedCfgCommand> computeCfg(String remoteAddress, String barcode) {
        if (remoteAddress == null || remoteAddress.length() == 0
                || barcode == null || barcode.length() == 0) {
            throw new InvalidDataException("Invalid remote address or barcode");
        }

        TokenData tokenData = getTokenData(remoteAddress, barcode);
        Tournament tournament = tokenData.getTournament();
        Player player = tokenData.getPlayer();

        String tournamentCfg = tournament.getCfg();
        List<ParsedCfgCommand> tournamentCfgCommands = new ParsedCfgCommandParser(tournamentCfg).parse();
        tournamentCfgCommands = new ParsedCfgCommandSanitizer(tournamentCfgCommands).removeSpecials();

        String playerCfgContent = "";

        Cfg cfg = cfgRepository.findByPlayerAndGame(player, tournament.getGame());
        if (cfg != null) {
            playerCfgContent = cfg.getCfgContent();
        }

        List<ParsedCfgCommand> playerCfgCommands = new ParsedCfgCommandParser(playerCfgContent).parse();
        playerCfgCommands = new ParsedCfgCommandSanitizer(playerCfgCommands).removeSpecials();

        List<ParsedCfgCommand> seatCfgCommands = new ParsedCfgCommandMerger(playerCfgCommands).mergeWithTournamentCfg(tournamentCfgCommands);
        seatCfgCommands = new ParsedCfgCommandSanitizer(seatCfgCommands).ensureMinimals();

        seatCfgCommands.add(ParsedCfgCommand.prepareName(player.getNickname()));

        return seatCfgCommands;
    }

    @Transactional
    public void saveCfg(String remoteAddress, String barcode, List<String> newCfg) {
        if (remoteAddress == null || remoteAddress.length() == 0
                || barcode == null || barcode.length() == 0
                || newCfg == null) {
            throw new InvalidDataException("Invalid remote address or barcode");
        }

        TokenData tokenData = getTokenData(remoteAddress, barcode);
        Tournament tournament = tokenData.getTournament();
        Player player = tokenData.getPlayer();

        Cfg cfg = cfgRepository.findByPlayerAndGame(player, tournament.getGame());
        if (cfg == null) {
            cfg = new Cfg(player, tournament.getGame());
        }

        cfg.setCfgContent(StringUtility.joinLines(newCfg));
        cfgRepository.save(cfg);
    }
}
