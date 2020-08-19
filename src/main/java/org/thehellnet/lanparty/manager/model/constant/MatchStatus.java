package org.thehellnet.lanparty.manager.model.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MatchStatus {

    SCHEDULED("Scheduled"),
    PLAYING("Playing now"),
    FINISHED("Finished");

    private final String description;

    MatchStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getName() {
        return this.name();
    }

    @Override
    public String toString() {
        return description;
    }
}
