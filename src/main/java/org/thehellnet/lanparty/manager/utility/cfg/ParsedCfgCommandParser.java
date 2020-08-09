package org.thehellnet.lanparty.manager.utility.cfg;

import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand;
import org.thehellnet.utility.StringUtility;

import java.util.*;

import static org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand.ONE_PARAMS_ACTIONS;
import static org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand.TWO_PARAMS_ACTIONS;

public class ParsedCfgCommandParser extends AbstractParsedCfgCommandUtility<String, List<ParsedCfgCommand>> {

    @Override
    protected void elaborate() {
        Map<Integer, ParsedCfgCommand> parsedCfgCommands = new LinkedHashMap<>();

        List<String> cfgLines = StringUtility.splitLines(input);

        if (cfgLines == null) {
            output = Collections.emptyList();
            return;
        }

        for (String cfgLine : cfgLines) {
            ParsedCfgCommand parsedCfgCommand = parseCommand(cfgLine);
            if (parsedCfgCommand != null) {
                parsedCfgCommands.put(parsedCfgCommand.hashCode(), parsedCfgCommand);
            }
        }

        output = new ArrayList<>(parsedCfgCommands.values());
    }

    static ParsedCfgCommand parseCommand(String command) {
        if (command == null || command.length() == 0) {
            return null;
        }

        if (command.contains("\n") || command.contains("\r")) {
            return null;
        }

        List<String> items = StringUtility.splitSpaces(command);
        if (items.isEmpty()) {
            return null;
        }

        String action = items.get(0).trim();
        ParsedCfgCommand parsedCfgCommand = new ParsedCfgCommand(action);

        if (!ONE_PARAMS_ACTIONS.contains(parsedCfgCommand.getAction()) && items.size() > 1) {
            if (TWO_PARAMS_ACTIONS.contains(parsedCfgCommand.getAction())) {
                String args = StringUtility.joinSpaces(items.subList(1, items.size()));
                parsedCfgCommand.setArgs(args);
            } else {
                String param = items.get(1).trim();
                parsedCfgCommand.setParam(param);

                if (items.size() > 2) {
                    String args = StringUtility.joinSpaces(items.subList(2, items.size()));
                    parsedCfgCommand.setArgs(args);
                }
            }
        }

        return parsedCfgCommand;
    }
}
