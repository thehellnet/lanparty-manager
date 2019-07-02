package org.thehellnet.lanparty.manager.exception.cfg;

import org.thehellnet.lanparty.manager.exception.LanPartyManagerException;

public abstract class CfgException extends LanPartyManagerException {

    public CfgException() {
    }

    public CfgException(String message) {
        super(message);
    }
}
