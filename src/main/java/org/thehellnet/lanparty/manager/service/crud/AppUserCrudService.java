package org.thehellnet.lanparty.manager.service.crud;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.AlreadyPresentException;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.utility.EmailUtility;
import org.thehellnet.utility.PasswordUtility;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class AppUserCrudService extends AbstractCrudService<AppUser, AppUserRepository> {

    public AppUserCrudService(AppUserRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public AppUser create(Map<String, Object> dto) {
        if (!dto.containsKey("email")) {
            throw new InvalidDataException("E-mail address not provided");
        }

        String email = (String) dto.get("email");
        if (!EmailUtility.validate(email)) {
            throw new InvalidDataException("E-mail address not valid");
        }

        AppUser appUser = repository.findByEmail(email);
        if (appUser != null) {
            throw new AlreadyPresentException("E-mail address already registered");
        }

        if (!dto.containsKey("password")) {
            throw new InvalidDataException("Password not provided");
        }

        String password = (String) dto.get("password");
        String encryptedPassword = PasswordUtility.hash(password);
        if (encryptedPassword == null) {
            throw new InvalidDataException("Password not valid");
        }

        appUser = new AppUser(email, encryptedPassword);

        String name = (String) dto.getOrDefault("name", null);
        appUser.setName(name);

        if (!dto.containsKey("appUserRoles")) {
            throw new InvalidDataException("appUserRoles not provided");
        }

        String[] appUserRoles = parseStringList(dto.get("appUserRoles"));
        Set<Role> roles = new HashSet<>();
        for (String roleName : appUserRoles) {
            roles.add(Role.valueOf(roleName));
        }
        appUser.setAppUserRoles(roles);

        String barcode = (String) dto.getOrDefault("barcode", null);
        appUser.setBarcode(barcode);

        appUser = repository.save(appUser);

        return appUser;
    }

    @Override
    @Transactional
    public AppUser update(Long id, Map<String, Object> dto) {
        AppUser appUser = findById(id);

        boolean changed = false;

        if (dto.containsKey("email")) {
            String email = (String) dto.get("email");
            email = email != null ? email.strip() : null;
            appUser.setEmail(email);
            changed = true;
        }

        if (dto.containsKey("password")) {
            String password = (String) dto.get("password");
            password = password != null ? password.strip() : null;
            String encryptedPassword = PasswordUtility.hash(password);
            if (encryptedPassword != null) {
                appUser.setPassword(encryptedPassword);
                changed = true;
            }
        }

        if (dto.containsKey("name")) {
            String name = (String) dto.get("name");
            name = name != null ? name.strip() : null;
            appUser.setName(name);
            changed = true;
        }

        if (dto.containsKey("appUserRoles")) {
            String[] appUserRoles = parseStringList(dto.get("appUserRoles"), false);
            if (appUserRoles != null) {
                Set<Role> roles = new HashSet<>();
                for (String roleName : appUserRoles) {
                    roles.add(Role.valueOf(roleName));
                }
                appUser.setAppUserRoles(roles);
                changed = true;
            }
        }

        if (dto.containsKey("barcode")) {
            String barcode = (String) dto.get("barcode");
            appUser.setBarcode(barcode);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return repository.save(appUser);
    }
}
