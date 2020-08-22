package org.thehellnet.lanparty.manager.model.dto.request.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class RegisterAuthRequestDTO extends AuthRequestDTO {

    @NotEmpty
    @Email
    protected String email;

    @NotEmpty
    protected String password;

    @NotEmpty
    protected String name;

    protected String nickname;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
