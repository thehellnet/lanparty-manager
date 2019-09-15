package org.thehellnet.lanparty.manager.exception.team;

import org.thehellnet.lanparty.manager.exception.LanPartyManagerException;

public abstract class TeamException extends LanPartyManagerException {

    public TeamException() {
    }

    public TeamException(String message) {
        super(message);
    }
}
