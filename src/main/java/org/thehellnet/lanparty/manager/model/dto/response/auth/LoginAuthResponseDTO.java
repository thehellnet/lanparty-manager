package org.thehellnet.lanparty.manager.model.dto.response.auth;

import org.joda.time.DateTime;

public class LoginAuthResponseDTO extends AuthResponseDTO {

    public Long id;
    public String token;
    public DateTime expiration;
}
