package org.thehellnet.lanparty.manager.model.constant;

public enum RoleName {

    PUBLIC,
    USER,
    ADMIN;

    public String getValue() {
        return String.format("ROLE_%s", this.name());
    }

    @Override
    public String toString() {
        return getValue();
    }
}
