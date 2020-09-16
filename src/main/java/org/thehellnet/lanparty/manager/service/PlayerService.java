package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.model.dto.response.registration.RegisterResponseRegistrationResponseDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.TournamentRepository;

@Service
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private final AppUserRepository appUserRepository;
    private final PlayerRepository playerRepository;
    private final TournamentRepository tournamentRepository;

    @Autowired
    public PlayerService(AppUserRepository appUserRepository,
                         PlayerRepository playerRepository,
                         TournamentRepository tournamentRepository) {
        this.appUserRepository = appUserRepository;
        this.playerRepository = playerRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional(readOnly = true)
    public boolean emailAvailable(String email) {
        AppUser appUser = appUserRepository.findByEmail(email);
        return appUser == null;
    }

    @Transactional(readOnly = true)
    public boolean nicknameAvailable(String nickname) {
        AppUser appUser = appUserRepository.findByNickname(nickname);
        return appUser == null;
    }

    @Transactional
    public RegisterResponseRegistrationResponseDTO register(String name, String email, String phone, boolean isPlayer, String nickname) {
        if (email == null || email.length() == 0) {
            email = generateFakeEmail(name);
        }

        AppUser appUser;

        appUser = appUserRepository.findByEmail(email);
        if (appUser != null) {
            return new RegisterResponseRegistrationResponseDTO(false, "E-mail già esistente");
        }

        appUser = appUserRepository.findByNickname(nickname);
        if (appUser != null) {
            return new RegisterResponseRegistrationResponseDTO(false, "Nickname già registrato");
        }

        appUser = new AppUser();
        appUser.setPassword("");
        appUser.setEmail(email);
        appUser.setName(name);
        appUser.setPhone(phone);

        if (isPlayer) {
            appUser.setNickname(nickname);
        }

        appUser = appUserRepository.save(appUser);

        String message = String.format("Utente \"%s\" registrato", appUser.getName());
        return new RegisterResponseRegistrationResponseDTO(true, message);
    }

    private static String generateFakeEmail(final String name) {
        String fakeName = name.toLowerCase()
                .replaceAll("\\s+", "")
                .replaceAll("[^\\p{ASCII}]", "");
        return String.format("%s-fake@thehellnet.org", fakeName);
    }
}
