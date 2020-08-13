package org.thehellnet.lanparty.manager.model.protocol;

import org.json.JSONObject;

public final class Command {

    private final ShowcaseNoun noun;
    private final ShowcaseVerb verb;
    private final JSONObject args;

    public Command(Command command, JSONObject args) {
        this(command.noun, command.verb, args);
    }

    public Command(ShowcaseNoun noun, ShowcaseVerb verb) {
        this(noun, verb, new JSONObject());
    }

    public Command(ShowcaseNoun noun, ShowcaseVerb verb, JSONObject args) {
        this.noun = noun;
        this.verb = verb;
        this.args = args;
    }

    public ShowcaseNoun getNoun() {
        return noun;
    }

    public ShowcaseVerb getVerb() {
        return verb;
    }

    public JSONObject getArgs() {
        return args;
    }
}
