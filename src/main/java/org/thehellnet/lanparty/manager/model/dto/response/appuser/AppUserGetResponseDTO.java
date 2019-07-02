package org.thehellnet.lanparty.manager.model.dto.response.appuser;

import org.thehellnet.lanparty.manager.model.persistence.AppUser;

public class AppUserGetResponseDTO {

    private AppUser appUser;

    public AppUserGetResponseDTO() {
    }

    public AppUserGetResponseDTO(AppUser appUser) {
        this.appUser = appUser;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
