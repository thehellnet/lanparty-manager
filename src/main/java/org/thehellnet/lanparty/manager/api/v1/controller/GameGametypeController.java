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
import org.thehellnet.lanparty.manager.model.dto.request.gamegametype.CreateGameGametypeRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.gamegametype.UpdateGameGametypeRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.service.GameGametypeServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.GameGametype;
import org.thehellnet.lanparty.manager.service.impl.GameGametypeService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/public/gameGametype")
public class GameGametypeController {

    private static final Logger logger = LoggerFactory.getLogger(GameGametypeController.class);

    private final GameGametypeService gameGametypeService;

    @Autowired
    public GameGametypeController(GameGametypeService gameGametypeService) {
        this.gameGametypeService = gameGametypeService;
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateGameGametypeRequestDTO dto) {
        GameGametypeServiceDTO serviceDTO = new GameGametypeServiceDTO(dto.game, dto.gametype, dto.tag);
        GameGametype gameGametype = gameGametypeService.create(serviceDTO);
        return ResponseEntity.created(URI.create("")).body(gameGametype);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        GameGametype gameGametype = gameGametypeService.read(id);
        return ResponseEntity.ok(gameGametype);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<GameGametype> gameGametypes = gameGametypeService.readAll();
        return ResponseEntity.ok(gameGametypes);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateGameGametypeRequestDTO dto) {
        GameGametypeServiceDTO serviceDTO = new GameGametypeServiceDTO(dto.game, dto.gametype, dto.tag);
        GameGametype gameGametype = gameGametypeService.update(id, serviceDTO);
        return ResponseEntity.ok(gameGametype);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        gameGametypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
