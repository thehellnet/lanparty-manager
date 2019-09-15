package org.thehellnet.lanparty.manager.exception.team;

public class TeamAlreadyExistsException extends TeamException {

    public TeamAlreadyExistsException() {
    }

    public TeamAlreadyExistsException(String message) {
        super(message);
    }
}
