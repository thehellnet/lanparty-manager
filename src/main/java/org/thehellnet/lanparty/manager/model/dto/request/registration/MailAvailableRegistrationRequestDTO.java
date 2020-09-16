package org.thehellnet.lanparty.manager.model.dto.request.registration;

public class MailAvailableRegistrationRequestDTO extends RegistrationRequestDTO {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
