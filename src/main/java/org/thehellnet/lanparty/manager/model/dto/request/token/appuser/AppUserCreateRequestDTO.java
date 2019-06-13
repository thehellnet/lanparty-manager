package org.thehellnet.lanparty.manager.model.dto.request.token.appuser;

import org.thehellnet.lanparty.manager.model.dto.request.token.TokenRequestDTO;

public class AppUserCreateRequestDTO extends TokenRequestDTO {

    private String email;
    private String password;
    private String name;

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
}
