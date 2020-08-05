package org.thehellnet.lanparty.manager.exception.logline;

import org.thehellnet.lanparty.manager.exception.LanPartyException;

public class LogLineParserException extends LanPartyException {

    public LogLineParserException() {
    }

    public LogLineParserException(String message) {
        super(message);
    }

    public LogLineParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogLineParserException(Throwable cause) {
        super(cause);
    }

    public LogLineParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
