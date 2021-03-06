package org.thehellnet.lanparty.manager.model.logline;

public enum LineEvent {

    INIT_GAME("Start of match"),
    SHUTDOWN_GAME("End of match"),
    JOIN("Player join match"),
    QUIT("Player quits"),
    DAMAGE("Player inflict damage"),
    KILL("Player kills"),
    SAY("Player says"),
    WEAPON("Weapon"),
    UNUSEFUL("Unuseful line");

    private final String description;

    LineEvent(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
