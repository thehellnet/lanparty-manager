package org.thehellnet.utility.cfg;

import org.thehellnet.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public final class CfgUtility {

    public static String sanitize(String tournamentCfg, String playerCfg) {
        if (tournamentCfg == null || playerCfg == null) {
            return "";
        }

        List<String> tournamentLines = StringUtility.splitLines(tournamentCfg);
        List<String> playerLines = StringUtility.splitLines(playerCfg);

        List<String> cfg = new ArrayList<>();

        for (String line : tournamentLines) {
            CfgCommand command = parseCommand(line);

            for (String playerLine : playerLines) {
                CfgCommand playerCommand = parseCommand(playerLine);

                if (playerCommand.same(command)) {
                    command.setArgs(playerCommand.getArgs());
                }
            }

            cfg.add(command.toString());
        }

        return StringUtility.joinLines(cfg);
    }

    public static CfgCommand parseCommand(String command) {
        if (command == null
                || command.length() == 0) {
            return null;
        }

        if (command.contains("\n") || command.contains("\r")) {
            return null;
        }

        List<String> items = StringUtility.splitSpaces(command);
        if (items.size() == 0) {
            return null;
        }

        CfgCommand cfgCommand = new CfgCommand(items.get(0).trim());

        if (items.size() > 1) {
            cfgCommand.setParam(items.get(1).trim());

            if (items.size() > 2) {
                String args = StringUtility.joinSpaces(items.subList(2, items.size()));
                cfgCommand.setArgs(args);
            }
        }

        return cfgCommand;
    }
}
