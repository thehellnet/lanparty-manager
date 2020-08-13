package org.thehellnet.lanparty.manager.model.protocol;

public enum Action {
    DISPLAY_PANE("Display Pane");

    private final String description;

    Action(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name();
    }

    @Override
    public String toString() {
        return description;
    }
}
