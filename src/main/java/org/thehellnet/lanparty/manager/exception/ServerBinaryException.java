package org.thehellnet.lanparty.manager.exception;

public class ServerBinaryException extends LanPartyException {

    public ServerBinaryException() {
    }

    public ServerBinaryException(String message) {
        super(message);
    }

    public ServerBinaryException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerBinaryException(Throwable cause) {
        super(cause);
    }

    public ServerBinaryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
