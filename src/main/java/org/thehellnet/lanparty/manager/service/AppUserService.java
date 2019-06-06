package org.thehellnet.lanparty.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.AppUserTokenRepository;
import org.thehellnet.utility.EmailUtility;
import org.thehellnet.utility.PasswordUtility;
import org.thehellnet.utility.TokenUtility;

import java.util.Arrays;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserTokenRepository appUserTokenRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, AppUserTokenRepository appUserTokenRepository) {
        this.appUserRepository = appUserRepository;
        this.appUserTokenRepository = appUserTokenRepository;
    }

    @Transactional(readOnly = true)
    public AppUser findByEmailAndPassword(String email, String password) {
        if (!EmailUtility.validateForLogin(email)) {
            return null;
        }

        AppUser appUser = appUserRepository.findByEmail(email);
        if (appUser == null) {
            return null;
        }

        if (!PasswordUtility.verify(appUser.getPassword(), password)) {
            return null;
        }

        return appUser;
    }

    @Transactional
    public AppUserToken newToken(AppUser appUser) {
        if (appUser == null) {
            return null;
        }

        String token = TokenUtility.generate();
        return appUserTokenRepository.save(new AppUserToken(token, appUser));
    }

    @Transactional(readOnly = true)
    public boolean hasAllRoles(AppUser appUser, Role... roles) {
        appUser = appUserRepository.getOne(appUser.getId());
        return appUser.getAppUserRoles().containsAll(Arrays.asList(roles));
    }

    @Transactional(readOnly = true)
    public boolean hasAnyRoles(AppUser appUser, Role... roles) {
        appUser = appUserRepository.getOne(appUser.getId());

        for (Role role : roles) {
            if (appUser.getAppUserRoles().contains(role)) {
                return true;
            }
        }

        return false;
    }

    @Transactional
    public boolean changePassword(AppUser appUser, String password) {
        if (appUser == null) {
            return false;
        }

        appUser = appUserRepository.findById(appUser.getId()).orElse(null);
        if (appUser == null) {
            return false;
        }

        String encryptedPassword = PasswordUtility.hash(password);
        appUser.setPassword(encryptedPassword);
        appUserRepository.save(appUser);

        return true;
    }
}
