package org.thehellnet.lanparty.manager.utility.cfg;

public interface ParsedCfgCommandUtility<I, O> {

    O elaborate(I input);
}
