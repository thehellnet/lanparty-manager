package org.thehellnet.lanparty.manager.model.helper;

import org.thehellnet.lanparty.manager.exception.InvalidDataException;
import org.thehellnet.lanparty.manager.utility.cfg.ParsedCfgCommandSerializer;

import java.util.List;
import java.util.Objects;

public class ParsedCfgCommand {

    public static final List<String> ONE_PARAMS_ACTIONS = List.of(
            "unbindall"
    );

    public static final List<String> TWO_PARAMS_ACTIONS = List.of(
            "name",
            "sensitivity",
            "say"
    );

    private String action = null;
    private String param = null;
    private String args = null;

    private ParsedCfgCommand() {
    }

    public ParsedCfgCommand(ParsedCfgCommand other) {
        action = clean(other.action);
        param = clean(other.param);
        args = clean(other.args);
    }

    public ParsedCfgCommand(String action) {
        this.action = clean(Objects.requireNonNull(action));
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
        if (name == null || name.length() == 0) {
            throw new InvalidDataException();
        }

        return new ParsedCfgCommand("name", null, name);
    }

    public static ParsedCfgCommand prepareSay(String message, Object... args) {
        return prepareSay(String.format(message, args));
    }

    public static ParsedCfgCommand prepareSay(String message) {
        if (message == null || message.length() == 0) {
            throw new InvalidDataException();
        }

        return new ParsedCfgCommand("say", null, message);
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
        return input == null ? null : input.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParsedCfgCommand that = (ParsedCfgCommand) o;
        return action.equals(that.action) &&
                ((param == null && that.param == null) || Objects.equals(param, that.param)) &&
                ((args == null && that.args == null) || Objects.equals(args, that.args));
    }

    @Override
    public int hashCode() {
        if (ONE_PARAMS_ACTIONS.contains(action)
                || TWO_PARAMS_ACTIONS.contains(action)) {
            return Objects.hash(action);
        }

        return Objects.hash(action, param);
    }

    @Override
    public String toString() {
        return ParsedCfgCommandSerializer.serializeCommand(this);
    }
}
