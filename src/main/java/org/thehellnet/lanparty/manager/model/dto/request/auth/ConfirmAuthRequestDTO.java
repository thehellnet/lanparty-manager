package org.thehellnet.lanparty.manager.model.dto.request.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class ConfirmAuthRequestDTO extends AuthRequestDTO {

    @NotEmpty
    @Email
    protected String email;

    @NotEmpty
    protected String confirmCode;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }
}
