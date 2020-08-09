package org.thehellnet.lanparty.manager.utility.cfg;

import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand;

import java.util.ArrayList;
import java.util.List;

public class ParsedCfgCommandMerger extends AbstractParsedCfgCommandUtility<ParsedCfgCommandMerger.MergeDTO, List<ParsedCfgCommand>> {

    public static final class MergeDTO {

        private final List<ParsedCfgCommand> tournamentCfgCommands;
        private final List<ParsedCfgCommand> playerCfgCommands;
        private final List<ParsedCfgCommand> overrideCfgCommands;

        public MergeDTO(List<ParsedCfgCommand> tournamentCfgCommands,
                        List<ParsedCfgCommand> playerCfgCommands,
                        List<ParsedCfgCommand> overrideCfgCommands) {
            this.tournamentCfgCommands = tournamentCfgCommands;
            this.playerCfgCommands = playerCfgCommands;
            this.overrideCfgCommands = overrideCfgCommands;
        }
    }

    @Override
    protected void elaborate() {
        output = new ArrayList<>();

        if (input == null
                || input.tournamentCfgCommands == null
                || input.playerCfgCommands == null
                || input.overrideCfgCommands == null) {
            return;
        }

        for (ParsedCfgCommand tournamentCfgCommand : input.tournamentCfgCommands) {
            ParsedCfgCommand parsedCfgCommand = tournamentCfgCommand;

            for (ParsedCfgCommand userCfgCommand : input.playerCfgCommands) {
                if (parsedCfgCommand.same(userCfgCommand)) {
                    parsedCfgCommand = userCfgCommand;
                }
            }

            for (ParsedCfgCommand overrideCfgCommand : input.overrideCfgCommands) {
                if (parsedCfgCommand.same(overrideCfgCommand)) {
                    parsedCfgCommand = overrideCfgCommand;
                }
            }

            output.add(parsedCfgCommand);
        }
    }
}
