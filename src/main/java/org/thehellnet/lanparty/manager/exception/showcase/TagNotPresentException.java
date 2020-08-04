package org.thehellnet.lanparty.manager.exception.showcase;

import org.thehellnet.lanparty.manager.exception.LanPartyException;

public class TagNotPresentException extends LanPartyException {

    public TagNotPresentException() {
    }

    public TagNotPresentException(String message) {
        super(message);
    }

    public TagNotPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagNotPresentException(Throwable cause) {
        super(cause);
    }

    public TagNotPresentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
