package org.thehellnet.lanparty.manager.exception.player;

public class PlayerAlreadyExistsException extends PlayerException {

    public PlayerAlreadyExistsException() {
    }

    public PlayerAlreadyExistsException(String message) {
        super(message);
    }
}
