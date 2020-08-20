package org.thehellnet.lanparty.manager.utility.cfg;

import org.thehellnet.lanparty.manager.constant.CfgConstant;

import java.util.ArrayList;

public class ParsedCfgCommandSpecialEnsurer extends AbstractParsedCfgCommandManipulator {

    @Override
    protected void elaborate() {
        output = new ArrayList<>();

        output.addAll(CfgConstant.INITIALS);
        copyInputToOutputExcludeMinimals();
        output.addAll(CfgConstant.FINALS);
    }
}
