package org.thehellnet.lanparty.manager.model.dto.request.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class LoginAuthRequestDTO extends AuthRequestDTO {

    @NotEmpty
    public String email;

    @NotEmpty
    public String password;
}
