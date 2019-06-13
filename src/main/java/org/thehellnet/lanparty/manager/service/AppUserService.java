package org.thehellnet.lanparty.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.appuser.*;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.dto.light.AppUserLight;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.AppUserTokenRepository;
import org.thehellnet.utility.EmailUtility;
import org.thehellnet.utility.PasswordUtility;
import org.thehellnet.utility.TokenUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public List<AppUserLight> getAll() {
        List<AppUserLight> appUserLights = new ArrayList<>();

        List<AppUser> appUsers = appUserRepository.findAll();
        for (AppUser appUser : appUsers) {
            AppUserLight appUserLight = new AppUserLight(appUser);
            appUserLights.add(appUserLight);
        }

        return appUserLights;
    }

    @Transactional(readOnly = true)
    public AppUser get(Long id) {
        return appUserRepository.findById(id).orElse(null);
    }

    @Transactional
    public AppUser create(String email, String password, String name) throws AppUserException {
        if (!EmailUtility.validate(email)) {
            throw new AppUserInvalidMailException();
        }

        AppUser appUser = appUserRepository.findByEmail(email);
        if (appUser != null) {
            throw new AppUserAlreadyPresentException();
        }

        String encryptedPassword = PasswordUtility.hash(password);
        if (encryptedPassword == null) {
            throw new AppUserInvalidPasswordException();
        }

        appUser = new AppUser(email, encryptedPassword);

        if (name != null && name.length() > 0) {
            appUser.setName(name);
        }

        appUser = appUserRepository.save(appUser);

        return appUser;
    }

    @Transactional
    public void save(Long id, String name, String email) throws AppUserNotFoundException {
        AppUser appUser = appUserRepository.findById(id).orElse(null);
        if (appUser == null) {
            throw new AppUserNotFoundException();
        }

        if (name != null && name.length() > 0) {
            appUser.setName(name);
        }

        if (EmailUtility.validate(email)) {
            appUser.setEmail(email);
        }

        appUserRepository.save(appUser);
    }

    @Transactional
    public void delete(Long id) throws AppUserNotFoundException {
        AppUser appUser = appUserRepository.findById(id).orElse(null);
        if (appUser == null) {
            throw new AppUserNotFoundException();
        }

        appUserRepository.delete(appUser);
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
