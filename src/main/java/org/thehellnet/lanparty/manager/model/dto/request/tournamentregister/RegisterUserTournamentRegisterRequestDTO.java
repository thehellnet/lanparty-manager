package org.thehellnet.lanparty.manager.model.dto.request.tournamentregister;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class RegisterUserTournamentRegisterRequestDTO extends TournamentRegisterRequestDTO {

    @NotEmpty
    @Email
    public String email;

    @NotEmpty
    public String password;

    @NotEmpty
    public String name;

    @NotEmpty
    public String nickname;
}
