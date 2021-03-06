package org.thehellnet.lanparty.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.thehellnet.lanparty.manager.exception.LanPartyException;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class UnchangedException extends LanPartyException {

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
