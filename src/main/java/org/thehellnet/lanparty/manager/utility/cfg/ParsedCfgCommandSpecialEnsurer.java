package org.thehellnet.lanparty.manager.utility.cfg;

import org.thehellnet.lanparty.manager.settings.CfgSettings;

import java.util.ArrayList;

public class ParsedCfgCommandSpecialEnsurer extends AbstractParsedCfgCommandManipulator {

    @Override
    protected void elaborate() {
        output = new ArrayList<>();

        output.addAll(CfgSettings.INITIALS);
        copyInputToOutputExcludeMinimals();
        output.addAll(CfgSettings.FINALS);
    }
}
