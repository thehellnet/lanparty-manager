package org.thehellnet.lanparty.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.AlreadyPresentException;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.model.constant.RoleName;
import org.thehellnet.lanparty.manager.model.dto.request.auth.ConfirmAuthRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.auth.LoginAuthRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.auth.RegisterAuthRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.response.auth.LoginAuthResponseDTO;
import org.thehellnet.lanparty.manager.model.dto.response.auth.RegisterAuthResponseDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.model.persistence.Role;
import org.thehellnet.lanparty.manager.model.template.AppUserRegistrationConfirmTemplate;
import org.thehellnet.lanparty.manager.repository.*;
import org.thehellnet.utility.EmailUtility;
import org.thehellnet.utility.PasswordUtility;
import org.thehellnet.utility.TokenUtility;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService extends AbstractService {

    private final AppUserTokenRepository appUserTokenRepository;
    private final RoleRepository roleRepository;

    private final TemplateService templateService;
    private final MailService mailService;

    @Autowired
    public AuthService(SeatRepository seatRepository,
                       PlayerRepository playerRepository,
                       AppUserRepository appUserRepository,
                       AppUserTokenRepository appUserTokenRepository,
                       RoleRepository roleRepository,
                       TemplateService templateService,
                       MailService mailService) {
        super(seatRepository, playerRepository, appUserRepository);
        this.appUserTokenRepository = appUserTokenRepository;
        this.roleRepository = roleRepository;
        this.templateService = templateService;
        this.mailService = mailService;
    }

    @Transactional(readOnly = true)
    public AppUser findByEnabledTrueAndEmail(String email) {
        if (!EmailUtility.validateForLogin(email)) {
            throw new NotFoundException();
        }

        AppUser appUser = appUserRepository.findByEnabledTrueAndEmail(email);
        if (appUser == null) {
            throw new NotFoundException();
        }

        return appUser;
    }

    @Transactional(readOnly = true)
    public AppUser findByEnabledTrueAndEmailAndPassword(String email, String password) {
        AppUser appUser = findByEnabledTrueAndEmail(email);
        if (!PasswordUtility.verify(appUser.getPassword(), password)) {
            throw new NotFoundException();
        }

//        if (!hasAllRoles(appUser, Role.ACTION_LOGIN)) {
//            throw new UnauthorizedException("User not authorized to login");
//        }

        return appUser;
    }

    @Transactional(readOnly = true)
    public boolean hasAllRoles(AppUser appUser, Role... roles) {
        return hasRoles(appUser, true, roles);
    }

    @Transactional(readOnly = true)
    public boolean hasAnyRoles(AppUser appUser, Role... roles) {
        return hasRoles(appUser, false, roles);
    }

    @Transactional
    public AppUserToken newToken(AppUser appUser) {
        if (appUser == null) {
            throw new InvalidDataException();
        }

        String token = TokenUtility.generate();
        return appUserTokenRepository.save(new AppUserToken(token, appUser));
    }

    @Transactional
    public LoginAuthResponseDTO login(LoginAuthRequestDTO requestDTO) {
        AppUser appUser = findByEnabledTrueAndEmailAndPassword(requestDTO.email, requestDTO.password);
        AppUserToken appUserToken = newToken(appUser);

        LoginAuthResponseDTO responseDTO = new LoginAuthResponseDTO();
        responseDTO.id = appUserToken.getId();
        responseDTO.token = appUserToken.getToken();
        responseDTO.expiration = appUserToken.getExpirationDateTime();
        return responseDTO;
    }

    @Transactional
    public RegisterAuthResponseDTO register(RegisterAuthRequestDTO requestDTO) {
        AppUser appUser = appUserRepository.findByEnabledTrueAndEmail(requestDTO.email);
        if (appUser != null) {
            throw new AlreadyPresentException();
        }

        String hashedPassword = PasswordUtility.hash(requestDTO.password);
        appUser = new AppUser(requestDTO.email, hashedPassword, requestDTO.name, requestDTO.nickname);

        Role publicRole = roleRepository.findByRoleName(RoleName.PUBLIC);
        appUser.getRoles().add(publicRole);

        appUser.generateConfirmCode();

        appUser = appUserRepository.save(appUser);

        String confirmCode = String.format("%s|%s", appUser.getEmail(), appUser.getConfirmCode());
        String confirmCodeUrl = Base64.getUrlEncoder().encodeToString(confirmCode.getBytes());
        String endpoint = String.format("/api/public/v1/auth/confirm/%s", confirmCodeUrl);
        String link = mailService.computeUrl(endpoint);
        AppUserRegistrationConfirmTemplate template = new AppUserRegistrationConfirmTemplate(appUser, link);
        String mailBody = templateService.render(template);
        mailService.sendHtml(appUser.getEmail(), "Conferma registrazione", mailBody);

        RegisterAuthResponseDTO responseDTO = new RegisterAuthResponseDTO();
        responseDTO.email = appUser.getEmail();
        responseDTO.name = appUser.getName();
        responseDTO.nickname = appUser.getNickname();

        return responseDTO;
    }

    @Transactional
    public void confirm(ConfirmAuthRequestDTO requestDTO) {
        AppUser appUser = appUserRepository.findByEnabledFalseAndEmailAndConfirmCode(requestDTO.email, requestDTO.confirmCode);
        if (appUser == null) {
            throw new NotFoundException();
        }

        appUser.confirm();
        appUserRepository.save(appUser);
    }

    private boolean hasRoles(AppUser appUser, boolean all, Role[] roles) {
        if (roles == null) {
            return false;
        }

        appUser = appUserRepository.getOne(appUser.getId());

        if (roles.length == 0) {
            return appUser.getRoles().size() == 0;
        }

        Map<Role, Boolean> roleBooleanMap = new HashMap<>();

        for (Role role : roles) {
            roleBooleanMap.put(role, appUser.getRoles().contains(role));
        }

        for (Boolean item : roleBooleanMap.values()) {
            if (all && !item) return false;
            if (!all && item) return true;
        }

        return all;
    }
}
