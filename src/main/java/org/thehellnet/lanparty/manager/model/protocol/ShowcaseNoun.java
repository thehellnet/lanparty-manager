package org.thehellnet.lanparty.manager.model.protocol;

public enum ShowcaseNoun {
    TEST("test"),
    PANE("pane");

    private final String cmd;

    ShowcaseNoun(String cmd) {
        this.cmd = cmd;
    }

    public static ShowcaseNoun parseCmd(String cmd) {
        for (ShowcaseNoun item : ShowcaseNoun.values()) {
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
