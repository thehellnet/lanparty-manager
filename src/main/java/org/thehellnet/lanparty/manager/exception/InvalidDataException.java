package org.thehellnet.lanparty.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.thehellnet.lanparty.manager.exception.LanPartyException;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidDataException extends LanPartyException {

    public InvalidDataException() {
    }

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDataException(Throwable cause) {
        super(cause);
    }

    public InvalidDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
