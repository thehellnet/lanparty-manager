package org.thehellnet.lanparty.manager.api.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.dto.request.game.CreateGameRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.game.UpdateGameRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.service.GameServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.service.crud.GameCrudService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/public/game")
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final GameCrudService gameCrudService;

    @Autowired
    public GameController(GameCrudService gameCrudService) {
        this.gameCrudService = gameCrudService;
    }

    @CheckToken
    @CheckRoles(Role.GAME_CREATE)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateGameRequestDTO dto) {
        GameServiceDTO serviceDTO = new GameServiceDTO(dto.tag, dto.name);
        Game game = gameCrudService.create(serviceDTO);
        return ResponseEntity.created(URI.create("")).body(game);
    }

    @CheckToken
    @CheckRoles(Role.GAME_READ)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        Game game = gameCrudService.read(id);
        return ResponseEntity.ok(game);
    }

    @CheckToken
    @CheckRoles(Role.GAME_READ)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<Game> games = gameCrudService.readAll();
        return ResponseEntity.ok(games);
    }

    @CheckToken
    @CheckRoles(Role.GAME_UPDATE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateGameRequestDTO dto) {
        GameServiceDTO serviceDTO = new GameServiceDTO(dto.tag, dto.name);
        Game game = gameCrudService.update(id, serviceDTO);
        return ResponseEntity.ok(game);
    }

    @CheckToken
    @CheckRoles(Role.GAME_DELETE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        gameCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
