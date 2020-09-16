package org.thehellnet.lanparty.manager.model.dto.response.registration;

public class RegisterResponseRegistrationResponseDTO extends RegistrationResponseDTO {

    private final boolean result;
    private final String message;

    public RegisterResponseRegistrationResponseDTO(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
