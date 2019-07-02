package org.thehellnet.lanparty.manager.exception.game;

import org.thehellnet.lanparty.manager.exception.LanPartyManagerException;

public abstract class GameException extends LanPartyManagerException {

    public GameException() {
    }

    public GameException(String message) {
        super(message);
    }
}
