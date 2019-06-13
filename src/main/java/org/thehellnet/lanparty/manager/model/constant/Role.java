package org.thehellnet.lanparty.manager.model.constant;

public enum Role {
    LOGIN("Can login for get tokens"),

    APPUSER_VIEW("Can view AppUsers"),
    APPUSER_ADMIN("Can admin AppUsers", APPUSER_VIEW),
    APPUSER_CHANGE_PASSWORD("Can change password"),

    READ_PUBLIC("Can read public data"),
    READ_PRIVATE("Can read his own data");

    private String description;
    private Role[] impliedRoles;

    Role(String description, Role... impliedRoles) {
        this.description = description;
    }

    public Role[] getImpliedRoles() {
        return impliedRoles;
    }

    @Override
    public String toString() {
        return description;
    }
}
