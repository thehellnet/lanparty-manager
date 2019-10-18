package org.thehellnet.lanparty.manager.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.AlreadyPresentException;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.dto.service.AppUserServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.AppUserTokenRepository;
import org.thehellnet.lanparty.manager.service.AbstractCrudService;
import org.thehellnet.utility.EmailUtility;
import org.thehellnet.utility.PasswordUtility;
import org.thehellnet.utility.TokenUtility;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class AppUserService extends AbstractCrudService<AppUser, AppUserServiceDTO, AppUserRepository> {

    private final AppUserTokenRepository appUserTokenRepository;

    public AppUserService(AppUserRepository repository, AppUserTokenRepository appUserTokenRepository) {
        super(repository);
        this.appUserTokenRepository = appUserTokenRepository;
    }

    @Override
    @Transactional
    public AppUser create(AppUserServiceDTO dto) {
        if (!EmailUtility.validate(dto.email)) {
            throw new InvalidDataException("E-mail address not valid");
        }

        AppUser appUser = repository.findByEmail(dto.email);
        if (appUser != null) {
            throw new AlreadyPresentException("E-mail address already registered");
        }

        String encryptedPassword = PasswordUtility.hash(dto.password);
        if (encryptedPassword == null) {
            throw new InvalidDataException("Password not valid");
        }

        appUser = new AppUser(dto.email, encryptedPassword);

        if (dto.name != null) {
            appUser.setName(dto.name);
        }

        if (dto.barcode != null) {
            appUser.setBarcode(dto.barcode);
        }

        appUser = repository.save(appUser);

        return appUser;
    }

    @Override
    @Transactional
    public AppUser update(Long id, AppUserServiceDTO dto) {
        AppUser appUser = findById(id);

        boolean changed = false;

        if (dto.name != null) {
            appUser.setName(dto.name.strip());
            changed = true;
        }

        if (dto.password != null && dto.password.length() > 0) {
            String encryptedPassword = PasswordUtility.hash(dto.password);
            appUser.setPassword(encryptedPassword);
            changed = true;
        }

        if (dto.appUserRoles != null) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : dto.appUserRoles) {
                roles.add(Role.valueOf(roleName));
            }
            appUser.setAppUserRoles(roles);
            changed = true;
        }

        if (dto.barcode != null) {
            appUser.setBarcode(dto.barcode.strip());
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return repository.save(appUser);
    }

    @Transactional(readOnly = true)
    public AppUser findByEmail(String email) {
        if (!EmailUtility.validateForLogin(email)) {
            throw new NotFoundException();
        }

        AppUser appUser = repository.findByEmail(email);
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
        AppUser appUser = repository.findByBarcode(barcode);
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

        appUser = repository.getOne(appUser.getId());

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
