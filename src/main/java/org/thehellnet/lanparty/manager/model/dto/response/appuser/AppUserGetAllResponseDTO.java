package org.thehellnet.lanparty.manager.model.dto.response.appuser;

import org.thehellnet.lanparty.manager.model.dto.light.AppUserLight;

import java.util.List;

public class AppUserGetAllResponseDTO {

    private List<AppUserLight> appUsers;

    public AppUserGetAllResponseDTO() {
    }

    public AppUserGetAllResponseDTO(List<AppUserLight> appUsers) {
        this.appUsers = appUsers;
    }

    public List<AppUserLight> getAppUsers() {
        return appUsers;
    }

    public void setAppUsers(List<AppUserLight> appUsers) {
        this.appUsers = appUsers;
    }
}
