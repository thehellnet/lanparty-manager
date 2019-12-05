package org.thehellnet.lanparty.manager.model.protocol;

import org.json.JSONObject;

public class CommandParser {

    private final String message;

    public CommandParser(String message) {
        this.message = message;
    }

    public Command parse() {
        JSONObject jsonObject = new JSONObject(message);

        ShowcaseNoun noun = ShowcaseNoun.parseCmd(jsonObject.getString("noun"));
        ShowcaseVerb verb = ShowcaseVerb.parseCmd(jsonObject.getString("verb"));
        JSONObject args = jsonObject.getJSONObject("args");

        return new Command(noun, verb, args);
    }
}
