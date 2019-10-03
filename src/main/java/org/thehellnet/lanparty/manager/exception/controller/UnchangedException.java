package org.thehellnet.lanparty.manager.exception.controller;

import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class UnchangedException extends ControllerException {

    public UnchangedException() {
    }

    public UnchangedException(String message) {
        super(message);
    }

    public UnchangedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnchangedException(Throwable cause) {
        super(cause);
    }

    public UnchangedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
