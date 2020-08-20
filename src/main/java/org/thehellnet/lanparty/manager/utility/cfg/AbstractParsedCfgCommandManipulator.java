package org.thehellnet.lanparty.manager.utility.cfg;

import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand;
import org.thehellnet.lanparty.manager.constant.CfgConstant;

import java.util.List;

public abstract class AbstractParsedCfgCommandManipulator extends AbstractParsedCfgCommandUtility<List<ParsedCfgCommand>, List<ParsedCfgCommand>> {

    protected void copyInputToOutputExcludeMinimals() {
        if (input == null) {
            return;
        }

        for (ParsedCfgCommand inputCfgCommand : input) {
            boolean includeCommand = true;
            for (ParsedCfgCommand minimalCfgCommand : CfgConstant.SPECIALS) {
                if (inputCfgCommand.same(minimalCfgCommand)) {
                    includeCommand = false;
                }
            }

            if (includeCommand) {
                output.add(inputCfgCommand);
            }
        }
    }
}
