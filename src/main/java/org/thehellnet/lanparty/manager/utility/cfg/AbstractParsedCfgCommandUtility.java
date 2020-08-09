package org.thehellnet.lanparty.manager.utility.cfg;

public abstract class AbstractParsedCfgCommandUtility<I, O> implements ParsedCfgCommandUtility<I, O> {

    protected I input;
    protected O output;

    @Override
    public synchronized O elaborate(I input) {
        this.input = input;
        elaborate();
        return output;
    }

    protected abstract void elaborate();
}
