package org.thehellnet.lanparty.manager.model.dto.response.appuser;

import org.joda.time.DateTime;

public class LoginAppUserResponseDTO extends AppUserResponseDTO {

    public String token;
    public DateTime expiration;
}
