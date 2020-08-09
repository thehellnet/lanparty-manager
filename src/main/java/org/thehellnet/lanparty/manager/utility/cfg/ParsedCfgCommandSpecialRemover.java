package org.thehellnet.lanparty.manager.utility.cfg;

import java.util.ArrayList;

public class ParsedCfgCommandSpecialRemover extends AbstractParsedCfgCommandManipulator {

    @Override
    protected void elaborate() {
        output = new ArrayList<>();

        copyInputToOutputExcludeMinimals();
    }
}
