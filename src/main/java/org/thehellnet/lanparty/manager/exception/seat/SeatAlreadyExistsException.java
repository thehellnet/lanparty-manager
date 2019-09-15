package org.thehellnet.lanparty.manager.exception.seat;

public class SeatAlreadyExistsException extends SeatException {

    public SeatAlreadyExistsException() {
    }

    public SeatAlreadyExistsException(String message) {
        super(message);
    }
}
