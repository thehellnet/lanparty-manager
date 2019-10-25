package org.thehellnet.lanparty.manager.api.v1.controller.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.dto.request.player.CreatePlayerRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.player.UpdatePlayerRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.service.PlayerServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Player;
import org.thehellnet.lanparty.manager.service.crud.PlayerCrudService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/crud/player")
public class PlayerCrudController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerCrudController.class);

    private final PlayerCrudService playerCrudService;

    @Autowired
    public PlayerCrudController(PlayerCrudService playerCrudService) {
        this.playerCrudService = playerCrudService;
    }

    @CheckToken
    @CheckRoles(Role.PLAYER_CREATE)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreatePlayerRequestDTO dto) {
        PlayerServiceDTO serviceDTO = new PlayerServiceDTO(dto.nickname, dto.appUser, dto.team);
        Player player = playerCrudService.create(serviceDTO);
        return ResponseEntity.created(URI.create("")).body(player);
    }

    @CheckToken
    @CheckRoles(Role.PLAYER_READ)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        Player player = playerCrudService.read(id);
        return ResponseEntity.ok(player);
    }

    @CheckToken
    @CheckRoles(Role.PLAYER_READ)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<Player> players = playerCrudService.readAll();
        return ResponseEntity.ok(players);
    }

    @CheckToken
    @CheckRoles(Role.PLAYER_UPDATE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdatePlayerRequestDTO dto) {
        PlayerServiceDTO serviceDTO = new PlayerServiceDTO(dto.nickname, dto.appUser, dto.team);
        Player player = playerCrudService.update(id, serviceDTO);
        return ResponseEntity.ok(player);
    }

    @CheckToken
    @CheckRoles(Role.PLAYER_DELETE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        playerCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
