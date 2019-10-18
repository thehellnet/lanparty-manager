package org.thehellnet.lanparty.manager.model.dto.service;

public class AppUserServiceDTO extends ServiceDTO {

    public String email;
    public String name;
    public String password;
    public String[] appUserRoles;
    public String barcode;

    public AppUserServiceDTO() {
    }

    public AppUserServiceDTO(String email, String name, String password, String[] appUserRoles, String barcode) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.appUserRoles = appUserRoles;
        this.barcode = barcode;
    }
}
