package org.thehellnet.lanparty.manager.model.spectator;

public enum SpectatorCommandAction {

    JOIN_SPECTATE("JoinSpectate"),
    SET_READY("SetReady"),
    NEXT_PLAYER("NextPlayer");

    private final String tag;

    SpectatorCommandAction(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return name();
    }
}
