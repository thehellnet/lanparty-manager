package org.thehellnet.lanparty.manager.service.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException;
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException;
import org.thehellnet.lanparty.manager.model.dto.service.PlayerServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Player;
import org.thehellnet.lanparty.manager.model.persistence.Team;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;
import org.thehellnet.lanparty.manager.repository.PlayerRepository;
import org.thehellnet.lanparty.manager.repository.TeamRepository;

@Service
public class PlayerService extends AbstractCrudService<Player, PlayerServiceDTO, PlayerRepository> {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private final AppUserRepository appUserRepository;
    private final TeamRepository teamRepository;

    public PlayerService(PlayerRepository repository, AppUserRepository appUserRepository, TeamRepository teamRepository) {
        super(repository);
        this.appUserRepository = appUserRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public Player create(PlayerServiceDTO dto) {
        if (dto.nickname == null) {
            throw new InvalidDataException("Invalid nickname");
        }

        if (dto.appUserId == null) {
            throw new InvalidDataException("Invalid appUser");
        }
        AppUser appUser = appUserRepository.findById(dto.appUserId).orElse(null);
        if (appUser == null) {
            throw new InvalidDataException("AppUser not found");
        }

        if (dto.teamId == null) {
            throw new InvalidDataException("Invalid team");
        }
        Team team = teamRepository.findById(dto.teamId).orElse(null);
        if (team == null) {
            throw new InvalidDataException("Team not found");
        }

        Player player = new Player(dto.nickname, appUser, team);
        player = repository.save(player);
        return player;
    }

    @Override
    public Player update(Long id, PlayerServiceDTO dto) {
        Player player = findById(id);

        boolean changed = false;

        if (dto.nickname != null) {
            player.setNickname(dto.nickname);
            changed = true;
        }

        if (dto.appUserId != null) {
            AppUser appUser = appUserRepository.findById(dto.appUserId).orElse(null);
            if (appUser == null) {
                throw new InvalidDataException("Invalid appUser");
            }
            player.setAppUser(appUser);
            changed = true;
        }

        if (dto.teamId != null) {
            Team team = teamRepository.findById(dto.teamId).orElse(null);
            if (team == null) {
                throw new InvalidDataException("Invalid team");
            }
            player.setTeam(team);
            changed = true;
        }

        if (!changed) {
            throw new UnchangedException();
        }

        return repository.save(player);
    }
}
