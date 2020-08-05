package org.thehellnet.lanparty.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.thehellnet.lanparty.manager.exception.LanPartyException;

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
