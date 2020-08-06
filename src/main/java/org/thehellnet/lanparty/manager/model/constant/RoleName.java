package org.thehellnet.lanparty.manager.model.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleName {

    PUBLIC,
    USER,
    ADMIN;

    @JsonValue
    public String getValue() {
        return String.format("ROLE_%s", this.name());
    }

    @Override
    public String toString() {
        return getValue();
    }
}
