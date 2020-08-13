package org.thehellnet.lanparty.manager.model.message.ws;

import org.springframework.web.socket.WebSocketMessage;
import org.thehellnet.lanparty.manager.model.protocol.Command;
import org.thehellnet.lanparty.manager.model.protocol.CommandSerializer;

public class CommandMessage implements WebSocketMessage<String> {

    private final Command command;
    private final String serializedCommand;

    public CommandMessage(Command command) {
        this.command = command;

        CommandSerializer commandSerializer = new CommandSerializer(this.command);
        this.serializedCommand = commandSerializer.serialize();
    }

    @Override
    public String getPayload() {
        return serializedCommand;
    }

    @Override
    public int getPayloadLength() {
        return serializedCommand.length();
    }

    @Override
    public boolean isLast() {
        return false;
    }
}
