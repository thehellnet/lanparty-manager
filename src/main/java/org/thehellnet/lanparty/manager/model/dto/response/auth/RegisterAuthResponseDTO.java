package org.thehellnet.lanparty.manager.model.dto.response.auth;

public class RegisterAuthResponseDTO extends AuthResponseDTO {

    protected String email;
    protected String name;
    protected String nickname;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
