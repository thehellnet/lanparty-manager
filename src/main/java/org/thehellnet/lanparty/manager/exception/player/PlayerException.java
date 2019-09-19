package org.thehellnet.lanparty.manager.exception.player;

import org.thehellnet.lanparty.manager.exception.LanPartyManagerException;

public abstract class PlayerException extends LanPartyManagerException {

    public PlayerException() {
    }

    public PlayerException(String message) {
        super(message);
    }
}
