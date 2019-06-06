package org.thehellnet.lanparty.manager.model.dto.request.token.appuser;

import org.thehellnet.lanparty.manager.model.dto.request.token.TokenRequestDTO;

public class AppUserChangePasswordRequestDTO extends TokenRequestDTO {

    private String oldPassword;
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
