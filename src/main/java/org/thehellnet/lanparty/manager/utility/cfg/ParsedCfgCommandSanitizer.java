package org.thehellnet.lanparty.manager.utility.cfg;

import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand;
import org.thehellnet.lanparty.manager.settings.CfgSettings;

import java.util.ArrayList;
import java.util.List;

public class ParsedCfgCommandSanitizer {

    private final List<ParsedCfgCommand> cfgCommands;

    public ParsedCfgCommandSanitizer(List<ParsedCfgCommand> cfgCommands) {
        this.cfgCommands = cfgCommands;
    }

    public List<ParsedCfgCommand> removeSpecials() {
        return sanitize(false);
    }

    public List<ParsedCfgCommand> ensureMinimals() {
        return sanitize(true);
    }

    private List<ParsedCfgCommand> sanitize(boolean includeMinimals) {
        List<ParsedCfgCommand> outputCfgCommands = new ArrayList<>();

        if (includeMinimals) {
            outputCfgCommands.addAll(CfgSettings.INITIALS);
        }

        filterSpecials(outputCfgCommands);

        if (includeMinimals) {
            outputCfgCommands.addAll(CfgSettings.FINALS);
        }

        return outputCfgCommands;
    }

    private void filterSpecials(List<ParsedCfgCommand> outputCfgCommands) {
        if (cfgCommands == null) {
            return;
        }

        for (ParsedCfgCommand parsedCfgCommand : cfgCommands) {
            boolean includeCommand = true;
            for (ParsedCfgCommand minimalCfgCommand : CfgSettings.SPECIALS) {
                if (parsedCfgCommand.same(minimalCfgCommand)) {
                    includeCommand = false;
                }
            }

            if (includeCommand) {
                outputCfgCommands.add(parsedCfgCommand);
            }
        }
    }
}
