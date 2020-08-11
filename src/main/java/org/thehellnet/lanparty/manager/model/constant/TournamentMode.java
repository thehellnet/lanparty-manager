package org.thehellnet.lanparty.manager.model.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TournamentMode {

    SINGLE_ELIMINATION("Single elimination"),
    ROUND_ROBIN("Round-Robin"),
    DOUBLE_ROUND_ROBIN("Double Round-Robin"),
    DOUBLE_ROUND_ROBIN_ELIMINATION("Double Round-Robin with elimination"),
    TEAMS_DOUBLE_ROUND_ROBIN_ELIMINATION("Teams Double Round-Robin with final elimination");

    private final String description;

    TournamentMode(String description) {
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
