package org.thehellnet.lanparty.manager.model.dto.request.appuser;

import org.thehellnet.lanparty.manager.model.dto.request.RequestDTO;

public class AppUserChangePasswordRequestDTO extends RequestDTO {

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
