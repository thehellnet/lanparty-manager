package org.thehellnet.lanparty.manager.model.dto.response.appuser;

import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.model.dto.response.ResponseDTO;

public class AppUserLoginResponseDTO extends ResponseDTO {

    private String token;
    private DateTime expiration;

    public String getToken() {
        return token;
    }

    public AppUserLoginResponseDTO setToken(String token) {
        this.token = token;
        return this;
    }

    public DateTime getExpiration() {
        return expiration;
    }

    public AppUserLoginResponseDTO setExpiration(DateTime expiration) {
        this.expiration = expiration;
        return this;
    }
}
