package org.thehellnet.lanparty.manager.model.dto.request.crud.save;

public class AppUserSaveCrudRequestDTO extends SaveCrudRequestDTO {

    private String name;
    private String[] appUserRoles;

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
