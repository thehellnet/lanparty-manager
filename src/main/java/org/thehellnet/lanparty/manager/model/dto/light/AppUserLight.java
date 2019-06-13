package org.thehellnet.lanparty.manager.model.dto.light;

import org.thehellnet.lanparty.manager.model.persistence.AppUser;

public class AppUserLight {

    private Long id;
    private String email;
    private String name;

    public AppUserLight() {
    }

    public AppUserLight(AppUser appUser) {
        if (appUser != null) {
            id = appUser.getId();
            email = appUser.getEmail();
            name = appUser.getName();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
