package org.thehellnet.lanparty.manager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.AppUserTokenRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.SeatRepository;
import org.thehellnet.utility.TokenUtility;

@Service
@Transactional
public class AppUserTokenService extends AbstractService {

    private final AppUserTokenRepository appUserTokenRepository;

    public AppUserTokenService(SeatRepository seatRepository,
                               PlayerRepository playerRepository,
                               AppUserRepository appUserRepository,
                               AppUserTokenRepository appUserTokenRepository) {
        super(seatRepository, playerRepository, appUserRepository);
        this.appUserTokenRepository = appUserTokenRepository;
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
        if (!TokenUtility.validateExpiration(appUserToken.getExpirationTs())) {
            return null;
        }

        AppUser appUser = appUserToken.getAppUser();

        //noinspection ResultOfMethodCallIgnored
        appUser.getAppUserTokens().size();

        return appUser;
    }
}
