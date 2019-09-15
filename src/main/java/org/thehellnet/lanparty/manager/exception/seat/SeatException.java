package org.thehellnet.lanparty.manager.exception.seat;

import org.thehellnet.lanparty.manager.exception.LanPartyManagerException;

public abstract class SeatException extends LanPartyManagerException {

    public SeatException() {
    }

    public SeatException(String message) {
        super(message);
    }
}
