package org.thehellnet.lanparty.manager.utility.cfg;

import org.thehellnet.lanparty.manager.exception.InvalidDataException;
import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand;
import org.thehellnet.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

import static org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand.ONE_PARAMS_ACTIONS;
import static org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand.TWO_PARAMS_ACTIONS;

public class ParsedCfgCommandSerializer {

    private final List<ParsedCfgCommand> cfgCommands;

    public ParsedCfgCommandSerializer(List<ParsedCfgCommand> cfgCommands) {
        this.cfgCommands = cfgCommands;
    }

    public String serialize() {
        return StringUtility.joinLines(serializeLines());
    }

    public List<String> serializeLines() {
        if (cfgCommands == null) {
            return new ArrayList<>();
        }

        List<String> cfgLines = new ArrayList<>();

        for (ParsedCfgCommand parsedCfgCommand : cfgCommands) {
            String cfgCommand = serializeCommand(parsedCfgCommand);
            if (cfgCommand != null) {
                cfgLines.add(cfgCommand);
            }
        }

        return cfgLines;
    }

    public static String serializeCommand(ParsedCfgCommand parsedCfgCommand) {
        if (parsedCfgCommand == null) {
            return null;
        }

        if (ONE_PARAMS_ACTIONS.contains(parsedCfgCommand.getAction())) {
            return parsedCfgCommand.getAction();
        } else if (TWO_PARAMS_ACTIONS.contains(parsedCfgCommand.getAction())) {
            if (parsedCfgCommand.getArgs() == null || parsedCfgCommand.getArgs().length() == 0) {
                throw new InvalidDataException("Two params command with no args");
            }
            return String.format("%s \"%s\"", parsedCfgCommand.getAction(), parsedCfgCommand.getArgs());
        }

        if (parsedCfgCommand.getParam() == null) {
            throw new InvalidDataException("Null param command");
        }

        String command = String.format("%s %s", parsedCfgCommand.getAction(), parsedCfgCommand.getParam());
        if (parsedCfgCommand.getArgs() != null && parsedCfgCommand.getArgs().length() > 0) {
            command += String.format(" \"%s\"", parsedCfgCommand.getArgs());
        }

        return command.strip();
    }
}
