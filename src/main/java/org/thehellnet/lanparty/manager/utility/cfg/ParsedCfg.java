package org.thehellnet.lanparty.manager.utility.cfg;

import org.thehellnet.lanparty.manager.exception.player.InvalidNamePlayerException;
import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand;
import org.thehellnet.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public class ParsedCfg {

    private final List<ParsedCfgCommand> commands;
    private final String playerNickname;

    public ParsedCfg(List<ParsedCfgCommand> commands, String playerNickname) throws InvalidNamePlayerException {
        this.commands = commands;

        if (playerNickname == null) {
            throw new InvalidNamePlayerException("Null player nickname");
        }

        this.playerNickname = playerNickname.trim();

        if (this.playerNickname.length() == 0) {
            throw new InvalidNamePlayerException("Empty player nickname");
        }
    }

    public List<ParsedCfgCommand> toCommandList() {
        return toCommandList(false);
    }

    public List<ParsedCfgCommand> toCommandList(boolean withMessage) {
        List<ParsedCfgCommand> commandList = new ArrayList<>();

        commandList.add(ParsedCfgCommand.UNBINDALL);

        if (commands != null) {
            commandList.addAll(commands);
        }

        commandList.add(ParsedCfgCommand.BIND_EXEC);
        commandList.add(ParsedCfgCommand.BIND_DUMP);

        commandList.add(ParsedCfgCommand.prepareName(playerNickname));

        if (withMessage) {
            String message = String.format("Player \"%s\" connected", playerNickname);
            commandList.add(ParsedCfgCommand.prepareSay(message));
        }

        return commandList;
    }

    public List<String> toStringList() {
        List<ParsedCfgCommand> commandList = toCommandList();
        List<String> stringList = new ArrayList<>();
        for (ParsedCfgCommand command : commandList) {
            stringList.add(command.toString());
        }
        return stringList;
    }

    @Override
    public String toString() {
        List<String> stringList = toStringList();
        return StringUtility.joinLines(stringList);
    }
}
