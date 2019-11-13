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
import org.thehellnet.lanparty.manager.model.dto.request.gamemap.CreateGameMapRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.gamemap.UpdateGameMapRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.service.GameMapServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.GameMap;
import org.thehellnet.lanparty.manager.service.crud.GameMapCrudServiceOLD;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/crud/gameMap")
public class GameMapCrudController {

    private static final Logger logger = LoggerFactory.getLogger(GameMapCrudController.class);

    private final GameMapCrudServiceOLD gameMapCrudService;

    @Autowired
    public GameMapCrudController(GameMapCrudServiceOLD gameMapCrudService) {
        this.gameMapCrudService = gameMapCrudService;
    }

    @CheckToken
    @CheckRoles(Role.GAMEMAP_CREATE)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateGameMapRequestDTO dto) {
        GameMapServiceDTO serviceDTO = new GameMapServiceDTO(dto.tag, dto.name, dto.game, dto.stock);
        GameMap gameMap = gameMapCrudService.create(serviceDTO);
        return ResponseEntity.created(URI.create("")).body(gameMap);
    }

    @CheckToken
    @CheckRoles(Role.GAMEMAP_READ)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        GameMap gameMap = gameMapCrudService.read(id);
        return ResponseEntity.ok(gameMap);
    }

    @CheckToken
    @CheckRoles(Role.GAMEMAP_READ)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<GameMap> gameMaps = gameMapCrudService.readAll();
        return ResponseEntity.ok(gameMaps);
    }

    @CheckToken
    @CheckRoles(Role.GAMEMAP_UPDATE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateGameMapRequestDTO dto) {
        GameMapServiceDTO serviceDTO = new GameMapServiceDTO(dto.tag, dto.name, dto.game, dto.stock);
        GameMap gameMap = gameMapCrudService.update(id, serviceDTO);
        return ResponseEntity.ok(gameMap);
    }

    @CheckToken
    @CheckRoles(Role.GAMEMAP_DELETE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        gameMapCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
