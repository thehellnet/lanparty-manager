package org.thehellnet.lanparty.manager.model.constant;

public enum Role {
    LOGIN("Can login for get tokens"),
    READ_PUBLIC("Can read public data"),
    READ_PRIVATE("Can read private data");

    private String description;

    Role(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
