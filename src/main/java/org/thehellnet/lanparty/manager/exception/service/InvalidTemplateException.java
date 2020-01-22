package org.thehellnet.lanparty.manager.exception.service;

import org.thehellnet.lanparty.manager.exception.LanPartyException;

public class InvalidTemplateException extends LanPartyException {

    public InvalidTemplateException() {
    }

    public InvalidTemplateException(String message) {
        super(message);
    }

    public InvalidTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTemplateException(Throwable cause) {
        super(cause);
    }

    public InvalidTemplateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
