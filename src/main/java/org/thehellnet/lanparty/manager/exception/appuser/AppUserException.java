package org.thehellnet.lanparty.manager.exception.appuser;

import org.thehellnet.lanparty.manager.exception.LanPartyManagerException;

public abstract class AppUserException extends LanPartyManagerException {
    public AppUserException() {
    }

    public AppUserException(String message) {
        super(message);
    }
}
