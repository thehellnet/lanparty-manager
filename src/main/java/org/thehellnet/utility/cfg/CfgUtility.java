package org.thehellnet.utility.cfg;

import org.thehellnet.utility.StringUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class CfgUtility {

    public static final String CFG_MINIMAL = StringUtility.joinLines(
            new ArrayList<>(
                    Arrays.asList(
                            CfgCommand.UNBINDALL.toString(),
                            CfgCommand.BIND_EXEC.toString(),
                            CfgCommand.BIND_DUMP.toString()
                    )
            )
    );

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

    public static String ensureRequired(String inputCfg) {
        if (inputCfg == null || inputCfg.length() == 0) {
            return CFG_MINIMAL;
        }

        List<String> lines = StringUtility.splitLines(inputCfg);
        if (lines.size() == 0) {
            return CFG_MINIMAL;
        }

        List<CfgCommand> cfgCommands = new ArrayList<>();

        cfgCommands.add(CfgCommand.UNBINDALL);

        for (String line : lines) {
            CfgCommand cfgCommand = parseCommand(line);

            if (cfgCommand.equals(CfgCommand.UNBINDALL)
                    || cfgCommand.equals(CfgCommand.BIND_EXEC)
                    || cfgCommand.equals(CfgCommand.BIND_DUMP)) {
                continue;
            }

            cfgCommands.add(cfgCommand);
        }

        cfgCommands.add(CfgCommand.BIND_EXEC);
        cfgCommands.add(CfgCommand.BIND_DUMP);

        List<String> stringList = cfgCommands.stream().map(CfgCommand::toString).collect(Collectors.toList());
        return StringUtility.joinLines(stringList);
    }
}
