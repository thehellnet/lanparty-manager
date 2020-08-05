package org.thehellnet.lanparty.manager.model.constant;

public enum FileHash {

    MD5("MD5"),
    SHA1("SHA-1"),
    SHA256("SHA-256");

    private final String description;

    FileHash(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
