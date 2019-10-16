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
    public Player create(String nickname, AppUser appUser, Team team) {
        if (nickname == null) {
            throw new InvalidDataException("Invalid nickname");
        }
        if (appUser == null) {
            throw new InvalidDataException("Invalid appUser");
        }
        if (team == null) {
            throw new InvalidDataException("Invalid team");
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
    public Player update(Long id, String nickname, AppUser appUser, Team team) {
        Player player = findById(id);

        boolean changed = false;

        if (nickname != null) {
            player.setNickname(nickname);
            changed = true;
        }

        if (appUser != null) {
            player.setAppUser(appUser);
            changed = true;
        }

        if (team != null) {
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
