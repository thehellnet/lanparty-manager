package org.thehellnet.lanparty.manager.exception.appuser;

public class AppUserAlreadyPresentException extends AppUserException {
    public AppUserAlreadyPresentException() {
    }

    public AppUserAlreadyPresentException(String message) {
        super(message);
    }
}
