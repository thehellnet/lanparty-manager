package org.thehellnet.lanparty.manager.exception.tournament;

import org.thehellnet.lanparty.manager.exception.LanPartyManagerException;

public abstract class TournamentException extends LanPartyManagerException {

    public TournamentException() {
    }

    public TournamentException(String message) {
        super(message);
    }
}
