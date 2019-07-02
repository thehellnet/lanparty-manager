package org.thehellnet.lanparty.manager.model.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {

    LOGIN("Can login for get tokens"),

    APPUSER_VIEW("Can view AppUsers"),
    APPUSER_ADMIN("Can admin AppUsers", APPUSER_VIEW),
    APPUSER_CHANGE_PASSWORD("Can change password"),

    TOURNAMENT_VIEW("Can view Tournaments"),
    TOURNAMENT_ADMIN("Can admin Tournaments", TOURNAMENT_VIEW),

    READ_PUBLIC("Can read public data"),
    READ_PRIVATE("Can read his own data");

    private String description;
    private Role[] impliedRoles;

    Role(String description, Role... impliedRoles) {
        this.description = description;
        this.impliedRoles = impliedRoles;
    }

    @JsonValue
    public String getName() {
        return name();
    }

    public Role[] getImpliedRoles() {
        return impliedRoles;
    }

    @Override
    public String toString() {
        return description;
    }
}
