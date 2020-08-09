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
import org.thehellnet.lanparty.manager.utility.cfg.*;

import java.util.List;

@Service
public class CfgService extends AbstractService {

    private final CfgRepository cfgRepository;

    private final ParsedCfgCommandUtility<String, List<ParsedCfgCommand>> parser = new ParsedCfgCommandParser();
    private final ParsedCfgCommandUtility<List<ParsedCfgCommand>, String> serializer = new ParsedCfgCommandSerializer();
    private final ParsedCfgCommandUtility<List<ParsedCfgCommand>, List<ParsedCfgCommand>> specialRemover = new ParsedCfgCommandSpecialRemover();
    private final ParsedCfgCommandUtility<List<ParsedCfgCommand>, List<ParsedCfgCommand>> specialEnsurer = new ParsedCfgCommandSpecialEnsurer();
    private final ParsedCfgCommandUtility<ParsedCfgCommandMerger.MergeDTO, List<ParsedCfgCommand>> merger = new ParsedCfgCommandMerger();

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
        List<ParsedCfgCommand> tournamentCfgCommands = parseAndRemoveSpecials(tournamentCfg);

        String playerCfg = "";

        Cfg cfg = cfgRepository.findByPlayerAndGame(player, tournament.getGame());
        if (cfg != null) {
            playerCfg = cfg.getCfgContent();
        }

        List<ParsedCfgCommand> playerCfgCommands = parseAndRemoveSpecials(playerCfg);

        String overrideCfg = tournament.getOverrideCfg();
        List<ParsedCfgCommand> overrideCfgCommands = parseAndRemoveSpecials(overrideCfg);

        ParsedCfgCommandMerger.MergeDTO mergeDTO = new ParsedCfgCommandMerger.MergeDTO(tournamentCfgCommands, playerCfgCommands, overrideCfgCommands);
        List<ParsedCfgCommand> seatCfgCommands = merger.elaborate(mergeDTO);

        seatCfgCommands = specialEnsurer.elaborate(seatCfgCommands);

        seatCfgCommands.add(ParsedCfgCommand.prepareName(player.getNickname()));

        return seatCfgCommands;
    }

    @Transactional
    public void saveCfg(String remoteAddress, String barcode, List<ParsedCfgCommand> parsedCfgCommands) {
        if (remoteAddress == null || remoteAddress.length() == 0
                || barcode == null || barcode.length() == 0
                || parsedCfgCommands == null) {
            throw new InvalidDataException("Invalid remote address or barcode");
        }

        TokenData tokenData = getTokenData(remoteAddress, barcode);
        Tournament tournament = tokenData.getTournament();
        Player player = tokenData.getPlayer();

        Cfg cfg = cfgRepository.findByPlayerAndGame(player, tournament.getGame());
        if (cfg == null) {
            cfg = new Cfg(player, tournament.getGame());
        }

        String cfgContent = serializer.elaborate(parsedCfgCommands);

        cfg.setCfgContent(cfgContent);
        cfgRepository.save(cfg);
    }

    private List<ParsedCfgCommand> parseAndRemoveSpecials(String cfg) {
        List<ParsedCfgCommand> parsedCfgCommands = parser.elaborate(cfg);
        return specialRemover.elaborate(parsedCfgCommands);
    }
}
