package org.thehellnet.lanparty.manager.model.dto.response.auth;


import java.time.LocalDateTime;

public class LoginAuthResponseDTO extends AuthResponseDTO {

    protected Long id;
    protected String token;
    protected LocalDateTime expiration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }
}
