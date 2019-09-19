package org.thehellnet.lanparty.manager.utility.cfg;

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

    public ParsedCfgCommand(String action) {
        setAction(action);
    }

    public ParsedCfgCommand(String action, String param) {
        this(action);
        setParam(param);
    }

    public ParsedCfgCommand(String action, String param, String args) {
        this(action, param);
        setArgs(args);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action != null ? action.trim() : "";
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param != null ? param.trim() : "";
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args != null ? args.trim() : "";
    }

    public boolean same(ParsedCfgCommand o) {
        return o != null
                && o.getAction().equals(action)
                && o.getParam().equals(param);
    }

    public static ParsedCfgCommand prepareName(String name) {
        return new ParsedCfgCommand("name", name);
    }

    public static ParsedCfgCommand prepareSay(String message) {
        return new ParsedCfgCommand("say", message);
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
        return Objects.hash(action, param);
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