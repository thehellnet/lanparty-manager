package org.thehellnet.lanparty.manager.model.constant;

public enum MatchStatus {
    SCHEDULED("Scheduled"),
    PLAYING("Playing now"),
    FINISHED("Finished");

    private final String description;

    MatchStatus(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
