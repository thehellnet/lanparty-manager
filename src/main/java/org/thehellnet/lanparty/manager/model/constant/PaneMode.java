package org.thehellnet.lanparty.manager.model.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaneMode {

    SCORES("Display tournament scores"),
    MATCHES("Display tournament matches and status"),
    SINGLE_MATCH("Display summary of single match");

    private final String description;

    PaneMode(String description) {
        this.description = description;
    }

    @JsonValue
    public String getName() {
        return name();
    }

    @Override
    public String toString() {
        return description;
    }
}
