package org.thehellnet.lanparty.manager.model.dto.service;

public class AppUserTokenServiceDTO extends ServiceDTO {

    public String token;
    public Long appUserId;

    public AppUserTokenServiceDTO() {
    }

    public AppUserTokenServiceDTO(String token, Long appUserId) {
        this.token = token;
        this.appUserId = appUserId;
    }
}
