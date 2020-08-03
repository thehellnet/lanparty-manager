package org.thehellnet.lanparty.manager.settings;

import org.apache.commons.lang3.ArrayUtils;
import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand;

public class CFGSettings {

    public static final ParsedCfgCommand UNBINDALL = new ParsedCfgCommand("unbindall");
    public static final ParsedCfgCommand BIND_QUIT = new ParsedCfgCommand("bind", "P", "quit");
    public static final ParsedCfgCommand BIND_EXEC = new ParsedCfgCommand("bind", ".", "exec lanpartytool");
    public static final ParsedCfgCommand BIND_DUMP = new ParsedCfgCommand("bind", ",", "writeconfig lanpartydump");

    public static final ParsedCfgCommand[] INITIALS = new ParsedCfgCommand[]{
            UNBINDALL
    };

    public static final ParsedCfgCommand[] FINALS = new ParsedCfgCommand[]{
            BIND_QUIT,
            BIND_EXEC,
            BIND_DUMP
    };

    public static final ParsedCfgCommand[] MINIMAL = ArrayUtils.addAll(INITIALS, FINALS);
}
