package org.thehellnet.lanparty.manager.model.constant;

public enum TournamentStatus {
    SCHEDULED("Scheduled"),
    PLAYING("Playing now"),
    FINISHED("Finished");

    private final String description;

    TournamentStatus(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
