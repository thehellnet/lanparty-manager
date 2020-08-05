package org.thehellnet.lanparty.manager.model.logline;

public enum LineEvent {

    INIT_GAME("InitGame - Start of match"),
    SHUTDOWN_GAME("ShutdownGame - End of match");

    private final String description;

    LineEvent(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
