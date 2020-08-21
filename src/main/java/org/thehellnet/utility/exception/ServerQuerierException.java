package org.thehellnet.utility.exception;

public class ServerQuerierException extends UtilityException {

    public ServerQuerierException() {
    }

    public ServerQuerierException(String message) {
        super(message);
    }

    public ServerQuerierException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerQuerierException(Throwable cause) {
        super(cause);
    }

    public ServerQuerierException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
