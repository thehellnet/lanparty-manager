package org.thehellnet.lanparty.manager.model.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FileHash {

    MD5("MD5"),
    SHA1("SHA-1"),
    SHA256("SHA-256");

    private final String description;

    FileHash(String description) {
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
