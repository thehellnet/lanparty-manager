package org.thehellnet.lanparty.manager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.AppUserTokenRepository;
import org.thehellnet.utility.EmailUtility;
import org.thehellnet.utility.PasswordUtility;
import org.thehellnet.utility.TokenUtility;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService extends AbstractService {

    private final AppUserRepository appUserRepository;
    private final AppUserTokenRepository appUserTokenRepository;

    public LoginService(AppUserRepository appUserRepository, AppUserTokenRepository appUserTokenRepository) {
        this.appUserRepository = appUserRepository;
        this.appUserTokenRepository = appUserTokenRepository;
    }

    @Transactional(readOnly = true)
    public AppUser findByEmail(String email) {
        if (!EmailUtility.validateForLogin(email)) {
            throw new NotFoundException();
        }

        AppUser appUser = appUserRepository.findByEmail(email);
        if (appUser == null) {
            throw new NotFoundException();
        }

        return appUser;
    }

    @Transactional(readOnly = true)
    public AppUser findByEmailAndPassword(String email, String password) {
        AppUser appUser = findByEmail(email);
        if (!PasswordUtility.verify(appUser.getPassword(), password)) {
            throw new NotFoundException();
        }

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

    private boolean hasRoles(AppUser appUser, boolean all, Role[] roles) {
        if (roles == null) {
            return false;
        }

        appUser = appUserRepository.getOne(appUser.getId());

        if (roles.length == 0) {
            return appUser.getAppUserRoles().size() == 0;
        }

        Map<Role, Boolean> roleBooleanMap = new HashMap<>();

        for (Role role : roles) {
            roleBooleanMap.put(role, appUser.getAppUserRoles().contains(role));
        }

        for (Boolean item : roleBooleanMap.values()) {
            if (all && !item) return false;
            if (!all && item) return true;
        }

        return all;
    }
}
