package org.thehellnet.lanparty.manager.model.spectator;

import org.json.JSONObject;
import org.thehellnet.lanparty.manager.exception.InvalidDataException;

public class SpectatorCommandSerializer {

    private final SpectatorCommand command;

    private SpectatorCommandSerializer(SpectatorCommand command) {
        this.command = command;
    }

    public static String serialize(SpectatorCommand command) {
        if (command == null) {
            throw new InvalidDataException();
        }

        SpectatorCommandSerializer serializer = new SpectatorCommandSerializer(command);
        return serializer.serialize();
    }

    private String serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", command.getAction().getTag());
        return jsonObject.toString();
    }
}
