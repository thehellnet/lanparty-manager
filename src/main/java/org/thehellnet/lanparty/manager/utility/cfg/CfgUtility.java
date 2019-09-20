package org.thehellnet.lanparty.manager.utility.cfg;

import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand;
import org.thehellnet.utility.StringUtility;

import java.util.*;
import java.util.stream.Collectors;

public final class CfgUtility {

    public static final String CFG_MINIMAL = StringUtility.joinLines(
            new ArrayList<>(
                    Arrays.asList(
                            ParsedCfgCommand.UNBINDALL.toString(),
                            ParsedCfgCommand.BIND_EXEC.toString(),
                            ParsedCfgCommand.BIND_DUMP.toString()
                    )
            )
    );

    public static List<ParsedCfgCommand> parseCfgFromString(String cfg) {
        return parseCfgFromStringList(StringUtility.splitLines(cfg));
    }

    public static List<ParsedCfgCommand> parseCfgFromStringList(List<String> cfgLines) {
        Map<Integer, ParsedCfgCommand> parsedCfgCommands = new LinkedHashMap<>();

        if (cfgLines != null) {
            for (String cfgLine : cfgLines) {
                ParsedCfgCommand parsedCfgCommand = parseCommand(cfgLine);
                if (parsedCfgCommand != null) {
                    parsedCfgCommands.put(parsedCfgCommand.hashCode(), parsedCfgCommand);
                }
            }
        }

        return new ArrayList<>(parsedCfgCommands.values());
    }

    public static List<ParsedCfgCommand> mergeTournamentWithPlayer(List<ParsedCfgCommand> tournamentCfgCommands, List<ParsedCfgCommand> playerCfgCommands) {
        List<ParsedCfgCommand> cfgCommands = new ArrayList<>();

        if (tournamentCfgCommands != null) {
            for (ParsedCfgCommand tournamentCfgCommand : tournamentCfgCommands) {
                ParsedCfgCommand cfgCommand = tournamentCfgCommand;

                if (playerCfgCommands != null) {
                    for (ParsedCfgCommand playerCfgCommand : playerCfgCommands) {
                        if (playerCfgCommand.same(tournamentCfgCommand)) {
                            cfgCommand = playerCfgCommand;
                        }
                    }
                }

                cfgCommands.add(cfgCommand);
            }
        }

        return cfgCommands;
    }

    public static List<ParsedCfgCommand> removeSpecialCommands(List<ParsedCfgCommand> inputCfgCommands) {
        List<ParsedCfgCommand> cfgCommands = new ArrayList<>();

        if (inputCfgCommands != null) {
            for (ParsedCfgCommand cfgCommand : inputCfgCommands) {
                if (cfgCommand.same(ParsedCfgCommand.UNBINDALL)
                        || cfgCommand.same(ParsedCfgCommand.BIND_EXEC)
                        || cfgCommand.same(ParsedCfgCommand.BIND_DUMP)
                        || cfgCommand.getAction().equals("name")) {
                    continue;
                }

                cfgCommands.add(cfgCommand);
            }
        }

        return cfgCommands;
    }

    public static ParsedCfgCommand parseCommand(String command) {
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

        ParsedCfgCommand parsedCfgCommand = new ParsedCfgCommand(items.get(0).trim());

        if (items.size() > 1) {
            parsedCfgCommand.setParam(items.get(1).trim());

            if (items.size() > 2) {
                String args = StringUtility.joinSpaces(items.subList(2, items.size()));
                parsedCfgCommand.setArgs(args);
            }
        }

        return parsedCfgCommand;
    }

    public static String addPlayerName(String playerName, String cfg) {
        String nameCmd = new ParsedCfgCommand("name", playerName).toString();
        return nameCmd + "\n" + cfg;
    }

    public static String sanitize(String tournamentCfg, String playerCfg) {
        if (tournamentCfg == null || playerCfg == null) {
            return "";
        }

        List<String> tournamentLines = StringUtility.splitLines(tournamentCfg);
        List<String> playerLines = StringUtility.splitLines(playerCfg);

        List<String> cfg = new ArrayList<>();

        for (String line : tournamentLines) {
            ParsedCfgCommand command = parseCommand(line);

            for (String playerLine : playerLines) {
                ParsedCfgCommand playerCommand = parseCommand(playerLine);

                if (playerCommand.same(command)) {
                    command.setArgs(playerCommand.getArgs());
                }
            }

            cfg.add(command.toString());
        }

        return StringUtility.joinLines(cfg);
    }

    public static String ensureRequired(String inputCfg) {
        if (inputCfg == null || inputCfg.length() == 0) {
            return CFG_MINIMAL;
        }

        List<String> lines = StringUtility.splitLines(inputCfg);
        if (lines.size() == 0) {
            return CFG_MINIMAL;
        }

        List<ParsedCfgCommand> parsedCfgCommands = new ArrayList<>();

        parsedCfgCommands.add(ParsedCfgCommand.UNBINDALL);

        for (String line : lines) {
            ParsedCfgCommand parsedCfgCommand = parseCommand(line);

            if (parsedCfgCommand.equals(ParsedCfgCommand.UNBINDALL)
                    || parsedCfgCommand.equals(ParsedCfgCommand.BIND_EXEC)
                    || parsedCfgCommand.equals(ParsedCfgCommand.BIND_DUMP)) {
                continue;
            }

            parsedCfgCommands.add(parsedCfgCommand);
        }

        parsedCfgCommands.add(ParsedCfgCommand.BIND_EXEC);
        parsedCfgCommands.add(ParsedCfgCommand.BIND_DUMP);

        List<String> stringList = parsedCfgCommands.stream().map(ParsedCfgCommand::toString).collect(Collectors.toList());
        return StringUtility.joinLines(stringList);
    }
}
