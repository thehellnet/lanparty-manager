package org.thehellnet.lanparty.manager.exception.controller;

import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyPresentException extends LanPartyException {

    public AlreadyPresentException() {
    }

    public AlreadyPresentException(String message) {
        super(message);
    }

    public AlreadyPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyPresentException(Throwable cause) {
        super(cause);
    }

    public AlreadyPresentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
