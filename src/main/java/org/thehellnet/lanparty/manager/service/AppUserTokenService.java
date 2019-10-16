package org.thehellnet.lanparty.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.AppUserTokenRepository;
import org.thehellnet.utility.TokenUtility;

import java.util.List;

@Service
public class AppUserTokenService extends AbstractService {

    private final AppUserTokenRepository appUserTokenRepository;
    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserTokenService(AppUserTokenRepository appUserTokenRepository, AppUserRepository appUserRepository) {
        this.appUserTokenRepository = appUserTokenRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public AppUserToken create(String token, AppUser appUser) {
        appUser = appUserRepository.findById(appUser.getId()).orElse(null);
        if (appUser != null) {
            throw new NotFoundException("E-mail address already registered");
        }

        AppUserToken appUserToken = new AppUserToken(token, appUser);

        appUserToken = appUserTokenRepository.save(appUserToken);

        return appUserToken;
    }

    @Transactional(readOnly = true)
    public AppUserToken get(Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public List<AppUserToken> getAll() {
        return appUserTokenRepository.findAll();
    }

    @Transactional
    public AppUserToken update(Long id, String token, AppUser appUser) {
        AppUserToken appUserToken = findById(id);

        boolean changed = false;

        if (token != null) {
            appUserToken.setToken(token.strip());
            changed = true;
        }


        if (appUser != null) {
            appUserToken.setAppUser(appUser);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return appUserTokenRepository.save(appUserToken);
    }

    @Transactional
    public void delete(Long id) {
        AppUserToken appUserToken = findById(id);
        appUserTokenRepository.delete(appUserToken);
    }

    @Transactional(readOnly = true)
    public AppUserToken findById(Long id) {
        AppUserToken appUserToken = appUserTokenRepository.findById(id).orElse(null);
        if (appUserToken == null) {
            throw new NotFoundException();
        }
        return appUserToken;
    }

    @Transactional(readOnly = true)
    public AppUser getAppUserByToken(String token) {
        if (token == null) {
            return null;
        }

        AppUserToken appUserToken = appUserTokenRepository.findByToken(token);
        if (appUserToken == null) {
            return null;
        }
        if (!TokenUtility.validateExpiration(appUserToken.getExpirationDateTime())) {
            return null;
        }

        return appUserToken.getAppUser();
    }
}
