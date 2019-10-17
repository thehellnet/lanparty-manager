package org.thehellnet.lanparty.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Player;
import org.thehellnet.lanparty.manager.model.persistence.Team;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.TeamRepository;

import java.util.List;

@Service
public class PlayerService extends AbstractService {

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

    @Transactional
    public Player create(String nickname, Long appUserId, Long teamId) {
        if (nickname == null) {
            throw new InvalidDataException("Invalid nickname");
        }

        if (appUserId == null) {
            throw new InvalidDataException("Invalid appUser");
        }
        AppUser appUser = appUserRepository.findById(appUserId).orElse(null);
        if (appUser == null) {
            throw new InvalidDataException("AppUser not found");
        }

        if (teamId == null) {
            throw new InvalidDataException("Invalid team");
        }
        Team team = teamRepository.findById(teamId).orElse(null);
        if (team == null) {
            throw new InvalidDataException("Team not found");
        }

        Player player = new Player(nickname, appUser, team);
        player = playerRepository.save(player);
        return player;
    }

    @Transactional(readOnly = true)
    public Player get(Long id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public List<Player> getAll() {
        return playerRepository.findAll();
    }

    @Transactional
    public Player update(Long id, String nickname, Long appUserId, Long teamId) {
        Player player = findById(id);

        boolean changed = false;

        if (nickname != null) {
            player.setNickname(nickname);
            changed = true;
        }

        if (appUserId != null) {
            AppUser appUser = appUserRepository.findById(appUserId).orElse(null);
            if (appUser == null) {
                throw new InvalidDataException("Invalid appUser");
            }
            player.setAppUser(appUser);
            changed = true;
        }

        if (teamId != null) {
            Team team = teamRepository.findById(teamId).orElse(null);
            if (team == null) {
                throw new InvalidDataException("Invalid team");
            }
            player.setTeam(team);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return playerRepository.save(player);
    }

    @Transactional
    public void delete(Long id) {
        Player player = findById(id);
        playerRepository.delete(player);
    }

    @Transactional(readOnly = true)
    public Player findById(Long id) {
        Player player = playerRepository.findById(id).orElse(null);
        if (player == null) {
            throw new NotFoundException();
        }
        return player;
    }
}
