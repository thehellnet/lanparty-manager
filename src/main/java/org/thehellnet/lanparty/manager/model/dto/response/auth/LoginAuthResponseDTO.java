package org.thehellnet.lanparty.manager.model.dto.response.auth;


import java.time.LocalDateTime;

public class LoginAuthResponseDTO extends AuthResponseDTO {

    public Long id;
    public String token;
    public LocalDateTime expiration;
}
