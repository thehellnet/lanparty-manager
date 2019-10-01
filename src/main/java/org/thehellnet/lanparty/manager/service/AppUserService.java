package org.thehellnet.lanparty.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.appuser.*;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.AppUserTokenRepository;
import org.thehellnet.utility.EmailUtility;
import org.thehellnet.utility.PasswordUtility;
import org.thehellnet.utility.TokenUtility;

import java.util.*;

@Service
public class AppUserService extends AbstractService {

    private final AppUserRepository appUserRepository;
    private final AppUserTokenRepository appUserTokenRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, AppUserTokenRepository appUserTokenRepository) {
        this.appUserRepository = appUserRepository;
        this.appUserTokenRepository = appUserTokenRepository;
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

    @Transactional(readOnly = true)
    public AppUser get(Long id) {
        return appUserRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<AppUser> getAll() {
        return appUserRepository.findAll();
    }

    @Transactional
    public AppUser update(Long id, String name, String password, String[] appUserRoles) throws AppUserNotFoundException {
        AppUser appUser = appUserRepository.findById(id).orElse(null);
        if (appUser == null) {
            throw new AppUserNotFoundException();
        }

        if (name != null && name.strip().length() > 0) {
            appUser.setName(name);
        }

        if (password != null && password.strip().length() > 0) {
            String encryptedPassword = PasswordUtility.hash(password);
            appUser.setPassword(encryptedPassword);
        }

        if (appUserRoles != null) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : appUserRoles) {
                roles.add(Role.valueOf(roleName));
            }
            appUser.setAppUserRoles(roles);
        }

        return appUserRepository.save(appUser);
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
    public AppUser findByEmail(String email) {
        if (!EmailUtility.validateForLogin(email)) {
            return null;
        }

        return appUserRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public AppUser findByEmailAndPassword(String email, String password) {
        AppUser appUser = findByEmail(email);
        if (appUser == null) {
            return null;
        }

        if (!PasswordUtility.verify(appUser.getPassword(), password)) {
            return null;
        }

        return appUser;
    }

    @Transactional(readOnly = true)
    public boolean hasAllRoles(AppUser appUser, Role... roles) {
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
            if (!item) {
                return false;
            }
        }

        return true;
    }

    @Transactional(readOnly = true)
    public boolean hasAnyRoles(AppUser appUser, Role... roles) {
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
            if (item) {
                return true;
            }
        }

        return false;
    }

    @Transactional
    public AppUserToken newToken(AppUser appUser) {
        if (appUser == null) {
            return null;
        }

        String token = TokenUtility.generate();
        return appUserTokenRepository.save(new AppUserToken(token, appUser));
    }
}
