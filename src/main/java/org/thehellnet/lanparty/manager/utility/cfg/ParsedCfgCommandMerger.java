package org.thehellnet.lanparty.manager.utility.cfg;

import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand;

import java.util.ArrayList;
import java.util.List;

public class ParsedCfgCommandMerger {

    private final List<ParsedCfgCommand> cfgCommands;

    public ParsedCfgCommandMerger(List<ParsedCfgCommand> cfgCommands) {
        this.cfgCommands = cfgCommands;
    }

    public List<ParsedCfgCommand> mergeWithTournamentCfg(List<ParsedCfgCommand> tournamentCfgCommands) {
        List<ParsedCfgCommand> outputCfgCommands = new ArrayList<>();

        if (tournamentCfgCommands != null) {
            for (ParsedCfgCommand tournamentCfgCommand : tournamentCfgCommands) {
                ParsedCfgCommand cfgCommand = tournamentCfgCommand;

                for (ParsedCfgCommand playerCfgCommand : cfgCommands) {
                    if (playerCfgCommand.same(tournamentCfgCommand)) {
                        cfgCommand = playerCfgCommand;
                    }
                }

                outputCfgCommands.add(cfgCommand);
            }
        }

        return outputCfgCommands;
    }
}
