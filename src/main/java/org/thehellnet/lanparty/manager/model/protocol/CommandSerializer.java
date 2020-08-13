package org.thehellnet.lanparty.manager.model.protocol;

import org.json.JSONObject;

public class CommandSerializer {

    private final Command command;

    public CommandSerializer(Command command) {
        this.command = command;
    }

    public static String serialize(Command command) {
        return new CommandSerializer(command).serialize();
    }

    public String serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", command.getAction().getName());
        jsonObject.put("args", command.getArgs());
        return jsonObject.toString();
    }
}
