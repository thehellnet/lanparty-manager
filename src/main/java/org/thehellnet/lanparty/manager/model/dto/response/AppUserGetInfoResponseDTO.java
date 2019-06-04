package org.thehellnet.lanparty.manager.model.dto.response;

import org.thehellnet.lanparty.manager.model.constant.Role;

import java.util.Set;

public class AppUserGetInfoResponseDTO extends ResponseDTO {

    private String name;
    private Set<Role> roles;

    public AppUserGetInfoResponseDTO(String name, Set<Role> roles) {
        this.name = name;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
