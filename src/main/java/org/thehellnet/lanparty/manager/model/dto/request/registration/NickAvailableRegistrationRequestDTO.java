package org.thehellnet.lanparty.manager.model.dto.request.registration;

public class NickAvailableRegistrationRequestDTO extends RegistrationRequestDTO {

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
