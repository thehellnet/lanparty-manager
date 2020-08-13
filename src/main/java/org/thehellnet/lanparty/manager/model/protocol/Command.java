package org.thehellnet.lanparty.manager.model.protocol;

import org.json.JSONObject;

public final class Command {

    private final Action action;

    private JSONObject args = new JSONObject();

    public Command(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public JSONObject getArgs() {
        return args;
    }

    public void setArgs(JSONObject args) {
        if (args == null) {
            this.args = new JSONObject();
        }

        this.args = args;
    }
}
