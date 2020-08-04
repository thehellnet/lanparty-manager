package org.thehellnet.lanparty.manager.settings;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.ArrayUtils;
import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand;

import java.util.List;

public class CfgSettings {

    private CfgSettings() {
    }

    public static final ParsedCfgCommand UNBINDALL = new ParsedCfgCommand("unbindall");
    public static final ParsedCfgCommand BIND_QUIT = new ParsedCfgCommand("bind", "P", "quit");
    public static final ParsedCfgCommand BIND_EXEC = new ParsedCfgCommand("bind", ".", "exec lanpartytool");
    public static final ParsedCfgCommand BIND_DUMP = new ParsedCfgCommand("bind", ",", "writeconfig lanpartydump");
    public static final ParsedCfgCommand NAME = new ParsedCfgCommand("name", null, "");

    private static final ParsedCfgCommand[] INITIALS_ARRAY = new ParsedCfgCommand[]{
            UNBINDALL
    };

    private static final ParsedCfgCommand[] FINALS_ARRAY = new ParsedCfgCommand[]{
            BIND_QUIT,
            BIND_EXEC,
            BIND_DUMP
    };

    private static final ParsedCfgCommand[] MINIMAL_ARRAY = ArrayUtils.addAll(INITIALS_ARRAY, FINALS_ARRAY);
    private static final ParsedCfgCommand[] SPECIALS_ARRAY = ArrayUtils.addAll(MINIMAL_ARRAY, NAME);

    public static final List<ParsedCfgCommand> INITIALS = ImmutableList.copyOf(INITIALS_ARRAY);
    public static final List<ParsedCfgCommand> FINALS = ImmutableList.copyOf(FINALS_ARRAY);
    public static final List<ParsedCfgCommand> MINIMAL = ImmutableList.copyOf(MINIMAL_ARRAY);
    public static final List<ParsedCfgCommand> SPECIALS = ImmutableList.copyOf(SPECIALS_ARRAY);
}
