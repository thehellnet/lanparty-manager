package org.thehellnet.lanparty.manager.exception.tournament;

public class TournamentAlreadyExistsException extends TournamentException {

    public TournamentAlreadyExistsException() {
    }

    public TournamentAlreadyExistsException(String message) {
        super(message);
    }
}
