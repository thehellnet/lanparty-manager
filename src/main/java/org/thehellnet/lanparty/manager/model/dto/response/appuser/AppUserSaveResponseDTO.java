package org.thehellnet.lanparty.manager.model.dto.response.appuser;

import org.thehellnet.lanparty.manager.model.persistence.AppUser;

public class AppUserSaveResponseDTO {

    private AppUser appUser;

    public AppUserSaveResponseDTO() {
    }

    public AppUserSaveResponseDTO(AppUser appUser) {
        this.appUser = appUser;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
