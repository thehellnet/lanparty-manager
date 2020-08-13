package org.thehellnet.lanparty.manager.model.protocol;

import org.json.JSONObject;

public class CommandSerializer {

    private final Command command;

    public CommandSerializer(Command command) {
        this.command = command;
    }

    public String serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("noun", command.getNoun().getCmd());
        jsonObject.put("verb", command.getVerb().getCmd());
        jsonObject.put("args", command.getArgs());
        return jsonObject.toString();
    }
}
