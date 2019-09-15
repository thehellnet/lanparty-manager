package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.appuser.AppUserNotFoundException;
import org.thehellnet.lanparty.manager.exception.player.PlayerAlreadyExistsException;
import org.thehellnet.lanparty.manager.exception.player.PlayerInvalidNickameOrTeamIDException;
import org.thehellnet.lanparty.manager.exception.player.PlayerNotFoundException;
import org.thehellnet.lanparty.manager.exception.team.TeamNotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Player;
import org.thehellnet.lanparty.manager.model.persistence.Team;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.TeamRepository;

@Service
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;
    private final AppUserRepository appUserRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, AppUserRepository appUserRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.appUserRepository = appUserRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional(readOnly = true)
    public Player findByBarcode(String barcode) {
        if (barcode == null || barcode.length() == 0) {
            return null;
        }

        return playerRepository.findByBarcode(barcode);
    }

    @Transactional
    public Player create(String nickname, Long teamId) throws PlayerInvalidNickameOrTeamIDException, PlayerAlreadyExistsException, TeamNotFoundException {
        if (nickname == null || nickname.length() == 0
                || teamId == null) {
            throw new PlayerInvalidNickameOrTeamIDException();
        }

        Player player = playerRepository.findByNickname(nickname);
        if (player != null) {
            throw new PlayerAlreadyExistsException();
        }

        Team team = teamRepository.findById(teamId).orElse(null);
        if (team == null) {
            throw new TeamNotFoundException();
        }

        player = new Player(nickname, team);
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
