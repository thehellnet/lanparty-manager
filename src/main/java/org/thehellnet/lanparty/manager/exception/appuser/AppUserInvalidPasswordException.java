package org.thehellnet.lanparty.manager.exception.appuser;

public class AppUserInvalidPasswordException extends AppUserException {

    public AppUserInvalidPasswordException() {
    }

    public AppUserInvalidPasswordException(String message) {
        super(message);
    }
}
