package org.thehellnet.lanparty.manager.model.protocol;

public enum ShowcaseVerb {
    PING("ping"),
    TEXT("text"),
    GET("get");

    private final String cmd;

    ShowcaseVerb(String cmd) {
        this.cmd = cmd;
    }

    public static ShowcaseVerb parseCmd(String cmd) {
        for (ShowcaseVerb item : ShowcaseVerb.values()) {
            if (item.getCmd().equals(cmd)) {
                return item;
            }
        }

        throw new IllegalArgumentException();
    }

    public String getCmd() {
        return cmd;
    }

    @Override
    public String toString() {
        return name();
    }
}
