package org.thehellnet.lanparty.manager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.AlreadyPresentException;
import org.thehellnet.lanparty.manager.model.constant.RoleName;
import org.thehellnet.lanparty.manager.model.dto.request.tournamentregister.RegisterUserTournamentRegisterRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.response.tournamentregister.RegisterUserTournamentRegisterResponseDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Role;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.RoleRepository;
import org.thehellnet.utility.PasswordUtility;

@Service
@Transactional
public class TournamentRegisterService extends AbstractService {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;

    public TournamentRegisterService(AppUserRepository appUserRepository, RoleRepository roleRepository) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public RegisterUserTournamentRegisterResponseDTO registerUser(RegisterUserTournamentRegisterRequestDTO requestDTO) {
        AppUser appUser = appUserRepository.findByEmail(requestDTO.email);
        if (appUser != null) {
            throw new AlreadyPresentException();
        }

        String hashedPassword = PasswordUtility.hash(requestDTO.password);
        appUser = new AppUser(requestDTO.email, hashedPassword, requestDTO.name, requestDTO.nickname);

        Role publicRole = roleRepository.findByRoleName(RoleName.PUBLIC);

        appUser.getRoles().add(publicRole);

        appUser.setEnabled(true);

        appUser = appUserRepository.save(appUser);

        RegisterUserTournamentRegisterResponseDTO responseDTO = new RegisterUserTournamentRegisterResponseDTO();
        responseDTO.Id = appUser.getId();
        responseDTO.email = appUser.getEmail();
        responseDTO.name = appUser.getName();
        responseDTO.nickname = appUser.getNickname();

        return responseDTO;
    }
}
