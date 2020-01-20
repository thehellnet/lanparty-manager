package org.thehellnet.lanparty.manager.model.dto.request.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class ConfirmAuthRequestDTO extends AuthRequestDTO {

    @NotEmpty
    @Email
    public String email;

    @NotEmpty
    public String confirmCode;
}
