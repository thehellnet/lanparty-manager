package org.thehellnet.lanparty.manager.model.helper;

import java.util.Objects;

public class ParsedCfgCommand {

    public static final ParsedCfgCommand UNBINDALL = new ParsedCfgCommand("unbindall");
    public static final ParsedCfgCommand BIND_EXEC = new ParsedCfgCommand("bind", ".", "exec lanpartytool");
    public static final ParsedCfgCommand BIND_DUMP = new ParsedCfgCommand("bind", ",", "writeconfig lanpartydump");

    private String action = "";
    private String param = "";
    private String args = "";

    public ParsedCfgCommand() {
    }

    public ParsedCfgCommand(ParsedCfgCommand other) {
        action = clean(other.action);
        param = clean(other.param);
        args = clean(other.args);
    }

    public ParsedCfgCommand(String action) {
        this.action = clean(action);
    }

    public ParsedCfgCommand(String action, String param) {
        this(action);
        this.param = clean(param);
    }

    public ParsedCfgCommand(String action, String param, String args) {
        this(action, param);
        this.args = clean(args);
    }

    public static ParsedCfgCommand prepareName(String name) {
        return new ParsedCfgCommand("name", name);
    }

    public static ParsedCfgCommand prepareSay(String message) {
        return new ParsedCfgCommand("say", message);
    }

    public String getAction() {
        return action;
    }

    public ParsedCfgCommand setAction(String action) {
        this.action = clean(action);
        return this;
    }

    public String getParam() {
        return param;
    }

    public ParsedCfgCommand setParam(String param) {
        this.param = clean(param);
        return this;
    }

    public String getArgs() {
        return args;
    }

    public ParsedCfgCommand setArgs(String args) {
        this.args = clean(args);
        return this;
    }

    public boolean same(ParsedCfgCommand o) {
        return o != null && o.hashCode() == this.hashCode();
    }

    public ParsedCfgCommand replace(String action) {
        ParsedCfgCommand parsedCfgCommand = new ParsedCfgCommand(this);
        parsedCfgCommand.setAction(action);
        return parsedCfgCommand;
    }

    public ParsedCfgCommand replace(String action, String param) {
        ParsedCfgCommand parsedCfgCommand = replace(action);
        parsedCfgCommand.setParam(param);
        return parsedCfgCommand;
    }

    public ParsedCfgCommand replace(String action, String param, String args) {
        ParsedCfgCommand parsedCfgCommand = replace(action, param);
        parsedCfgCommand.setArgs(args);
        return parsedCfgCommand;
    }

    public ParsedCfgCommand replaceAction(String action) {
        return replace(action);
    }

    public ParsedCfgCommand replaceParam(String param) {
        return replace(action).setParam(param);
    }

    public ParsedCfgCommand replaceArgs(String args) {
        return replace(action).setArgs(args);
    }

    private String clean(String input) {
        return input != null ? input.trim() : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParsedCfgCommand that = (ParsedCfgCommand) o;
        return action.equals(that.action) &&
                param.equals(that.param) &&
                args.equals(that.args);
    }

    @Override
    public int hashCode() {
        switch (action) {
            case "unbindall":
            case "name":
            case "sensitivity":
            case "say":
                return Objects.hash(action);

            default:
                return Objects.hash(action, param);
        }

    }

    @Override
    public String toString() {
        String string = String.format("%s %s", action, param);
        if (args.length() > 0) {
            string += String.format(" \"%s\"", args);
        }

        return string.trim();
    }
}
