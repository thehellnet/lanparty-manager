package org.thehellnet.lanparty.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.AlreadyPresentException;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
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
    public AppUser create(String email, String password, String name, String barcode) {
        if (!EmailUtility.validate(email)) {
            throw new InvalidDataException("E-mail address not valid");
        }

        AppUser appUser = appUserRepository.findByEmail(email);
        if (appUser != null) {
            throw new AlreadyPresentException("E-mail address already registered");
        }

        String encryptedPassword = PasswordUtility.hash(password);
        if (encryptedPassword == null) {
            throw new InvalidDataException("Password not valid");
        }

        appUser = new AppUser(email, encryptedPassword);

        if (name != null) {
            appUser.setName(name);
        }

        if (barcode != null) {
            appUser.setBarcode(barcode);
        }

        appUser = appUserRepository.save(appUser);

        return appUser;
    }

    @Transactional(readOnly = true)
    public AppUser get(Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public List<AppUser> getAll() {
        return appUserRepository.findAll();
    }

    @Transactional
    public AppUser update(Long id, String name, String password, String[] appUserRoles, String barcode) {
        AppUser appUser = findById(id);

        boolean changed = false;

        if (name != null) {
            appUser.setName(name.strip());
            changed = true;
        }

        if (password != null && password.length() > 0) {
            String encryptedPassword = PasswordUtility.hash(password);
            appUser.setPassword(encryptedPassword);
            changed = true;
        }

        if (appUserRoles != null) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : appUserRoles) {
                roles.add(Role.valueOf(roleName));
            }
            appUser.setAppUserRoles(roles);
            changed = true;
        }

        if (barcode != null) {
            appUser.setBarcode(barcode.strip());
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return appUserRepository.save(appUser);
    }

    @Transactional
    public void delete(Long id) {
        AppUser appUser = findById(id);
        appUserRepository.delete(appUser);
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
    public AppUser findByBarcode(String barcode) {
        AppUser appUser = appUserRepository.findByBarcode(barcode);
        if (appUser == null) {
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

    @Transactional(readOnly = true)
    public AppUser findById(Long id) {
        AppUser appUser = appUserRepository.findById(id).orElse(null);
        if (appUser == null) {
            throw new NotFoundException();
        }
        return appUser;
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
