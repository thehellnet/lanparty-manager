package org.thehellnet.lanparty.manager.exception.server;

import org.thehellnet.lanparty.manager.exception.LanPartyException;

public class WrongServerException extends LanPartyException {

    public WrongServerException() {
    }

    public WrongServerException(String message) {
        super(message);
    }

    public WrongServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongServerException(Throwable cause) {
        super(cause);
    }

    public WrongServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
