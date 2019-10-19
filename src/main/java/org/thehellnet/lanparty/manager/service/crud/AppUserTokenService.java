package org.thehellnet.lanparty.manager.service.crud;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.dto.service.AppUserTokenServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.AppUserTokenRepository;
import org.thehellnet.utility.TokenUtility;

@Service
public class AppUserTokenService extends AbstractCrudService<AppUserToken, AppUserTokenServiceDTO, AppUserTokenRepository> {

    private final AppUserTokenRepository appUserTokenRepository;
    private final AppUserRepository appUserRepository;

    public AppUserTokenService(AppUserTokenRepository repository, AppUserTokenRepository appUserTokenRepository, AppUserRepository appUserRepository) {
        super(repository);
        this.appUserTokenRepository = appUserTokenRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppUserToken create(AppUserTokenServiceDTO dto) {
        if (dto.appUserId == null) {
            throw new InvalidDataException("Invalid appUser");
        }
        AppUser appUser = appUserRepository.findById(dto.appUserId).orElse(null);
        if (appUser != null) {
            throw new InvalidDataException("AppUser not found");
        }

        AppUserToken appUserToken = new AppUserToken(dto.token, appUser);

        appUserToken = appUserTokenRepository.save(appUserToken);

        return appUserToken;
    }

    @Override
    public AppUserToken update(Long id, AppUserTokenServiceDTO dto) {
        AppUserToken appUserToken = findById(id);

        boolean changed = false;

        if (dto.token != null) {
            appUserToken.setToken(dto.token.strip());
            changed = true;
        }

        if (dto.appUserId != null) {
            AppUser appUser = appUserRepository.findById(dto.appUserId).orElse(null);
            if (appUser != null) {
                throw new InvalidDataException("AppUser not found");
            }
            appUserToken.setAppUser(appUser);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return appUserTokenRepository.save(appUserToken);

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
