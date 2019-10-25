package org.thehellnet.lanparty.manager.model.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ShowcaseMode {

    SCORES("Display scores"),
    MATCHES("Display matches and status"),
    SINGLE_MATCH("Display summary of single match");

    private final String description;

    ShowcaseMode(String description) {
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
