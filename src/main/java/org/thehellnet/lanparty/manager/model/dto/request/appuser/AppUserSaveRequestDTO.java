package org.thehellnet.lanparty.manager.model.dto.request.appuser;

import org.thehellnet.lanparty.manager.model.dto.request.RequestDTO;

public class AppUserSaveRequestDTO extends RequestDTO {

    private Long id;
    private String name;
    private String[] appUserRoles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String[] getAppUserRoles() {
        return appUserRoles;
    }

    public void setAppUserRoles(String[] appUserRoles) {
        this.appUserRoles = appUserRoles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
