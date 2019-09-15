package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.appuser.AppUserNotFoundException;
import org.thehellnet.lanparty.manager.exception.player.PlayerAlreadyExistsException;
import org.thehellnet.lanparty.manager.exception.player.PlayerInvalidNickameException;
import org.thehellnet.lanparty.manager.exception.player.PlayerNotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Player;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;

@Service
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;
    private final AppUserRepository appUserRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, AppUserRepository appUserRepository) {
        this.playerRepository = playerRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional(readOnly = true)
    public Player findByBarcode(String barcode) {
        if (barcode == null || barcode.length() == 0) {
            return null;
        }

        return playerRepository.findByBarcode(barcode);
    }

    @Transactional
    public Player create(String nickname) throws PlayerInvalidNickameException, PlayerAlreadyExistsException {
        if (nickname == null || nickname.length() == 0) {
            throw new PlayerInvalidNickameException();
        }

        Player player = playerRepository.findByNickname(nickname);
        if (player != null) {
            throw new PlayerAlreadyExistsException();
        }

        player = new Player(nickname);
        player = playerRepository.save(player);
        return player;
    }

    @Transactional
    public Player save(Long id, String nickname, String barcode, Long appUserId) throws PlayerNotFoundException, AppUserNotFoundException {
        Player player = playerRepository.findById(id).orElse(null);
        if (player == null) {
            throw new PlayerNotFoundException();
        }

        if (nickname != null && nickname.length() > 0) {
            player.setNickname(nickname);
        }

        if (barcode != null && barcode.length() > 0) {
            player.setBarcode(barcode);
        }

        if (appUserId != null) {
            AppUser appUser = appUserRepository.findById(appUserId).orElse(null);
            if (appUser == null) {
                throw new AppUserNotFoundException();
            }
            player.setAppUser(appUser);
        }

        return playerRepository.save(player);
    }
}