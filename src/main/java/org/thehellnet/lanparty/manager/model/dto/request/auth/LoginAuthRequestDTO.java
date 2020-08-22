package org.thehellnet.lanparty.manager.model.dto.request.auth;

import javax.validation.constraints.NotEmpty;

public class LoginAuthRequestDTO extends AuthRequestDTO {

    @NotEmpty
    protected String email;

    @NotEmpty
    protected String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
