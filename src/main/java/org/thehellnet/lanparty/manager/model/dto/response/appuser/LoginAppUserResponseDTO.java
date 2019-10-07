package org.thehellnet.lanparty.manager.model.dto.response.appuser;

import org.joda.time.DateTime;

public class LoginAppUserResponseDTO extends AppUserResponseDTO {

    public Long id;
    public String token;
    public DateTime expiration;
}
