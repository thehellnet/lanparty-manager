package org.thehellnet.lanparty.manager.model.spectator;

import java.util.Objects;

public class SpectatorCommand {

    private final SpectatorCommandAction action;

    public SpectatorCommand(SpectatorCommandAction action) {
        this.action = action;
    }

    public SpectatorCommandAction getAction() {
        return action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpectatorCommand that = (SpectatorCommand) o;
        return action == that.action;
    }

    @Override
    public int hashCode() {
        return Objects.hash(action);
    }

    @Override
    public String toString() {
        return action.getTag();
    }
}
