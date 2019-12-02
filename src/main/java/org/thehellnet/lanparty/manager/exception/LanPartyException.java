package org.thehellnet.lanparty.manager.exception;

public abstract class LanPartyException extends RuntimeException {

    public LanPartyException() {
    }

    public LanPartyException(String message) {
        super(message);
    }

    public LanPartyException(String message, Throwable cause) {
        super(message, cause);
    }

    public LanPartyException(Throwable cause) {
        super(cause);
    }

    public LanPartyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
