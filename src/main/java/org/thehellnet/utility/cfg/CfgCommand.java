package org.thehellnet.utility.cfg;

import java.util.Objects;

public class CfgCommand {

    private String action = "";
    private String param = "";
    private String args = "";

    public CfgCommand() {
    }

    public CfgCommand(String action) {
        setAction(action);
    }

    public CfgCommand(String action, String param) {
        this(action);
        setParam(param);
    }

    public CfgCommand(String action, String param, String args) {
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

    public boolean same(CfgCommand o) {
        return o != null
                && o.getAction().equals(action)
                && o.getParam().equals(param);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CfgCommand that = (CfgCommand) o;
        return action.equals(that.action) &&
                param.equals(that.param) &&
                args.equals(that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, param, args);
    }

    @Override
    public String toString() {
        String string = String.format("%s %s", action, param);
        if (args.length() > 0) {
            string += String.format(" \"%s\"", args);
        }

        return string;
    }
}
