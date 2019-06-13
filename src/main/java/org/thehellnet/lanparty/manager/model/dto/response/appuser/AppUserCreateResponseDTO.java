package org.thehellnet.lanparty.manager.model.dto.response.appuser;

import org.thehellnet.lanparty.manager.model.persistence.AppUser;

public class AppUserCreateResponseDTO {

    private AppUser appUser;

    public AppUserCreateResponseDTO() {
    }

    public AppUserCreateResponseDTO(AppUser appUser) {
        this.appUser = appUser;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
