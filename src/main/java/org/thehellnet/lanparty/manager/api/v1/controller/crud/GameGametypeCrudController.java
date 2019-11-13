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
import org.thehellnet.lanparty.manager.model.dto.request.gamegametype.CreateGameGametypeRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.gamegametype.UpdateGameGametypeRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.service.GameGametypeServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.GameGametype;
import org.thehellnet.lanparty.manager.service.crud.GameGametypeCrudServiceOLD;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/crud/gameGametype")
public class GameGametypeCrudController {

    private static final Logger logger = LoggerFactory.getLogger(GameGametypeCrudController.class);

    private final GameGametypeCrudServiceOLD gameGametypeCrudService;

    @Autowired
    public GameGametypeCrudController(GameGametypeCrudServiceOLD gameGametypeCrudService) {
        this.gameGametypeCrudService = gameGametypeCrudService;
    }

    @CheckToken
    @CheckRoles(Role.GAMEGAMETYPE_CREATE)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateGameGametypeRequestDTO dto) {
        GameGametypeServiceDTO serviceDTO = new GameGametypeServiceDTO(dto.game, dto.gametype, dto.tag);
        GameGametype gameGametype = gameGametypeCrudService.create(serviceDTO);
        return ResponseEntity.created(URI.create("")).body(gameGametype);
    }

    @CheckToken
    @CheckRoles(Role.GAMEGAMETYPE_READ)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        GameGametype gameGametype = gameGametypeCrudService.read(id);
        return ResponseEntity.ok(gameGametype);
    }

    @CheckToken
    @CheckRoles(Role.GAMEGAMETYPE_READ)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<GameGametype> gameGametypes = gameGametypeCrudService.readAll();
        return ResponseEntity.ok(gameGametypes);
    }

    @CheckToken
    @CheckRoles(Role.GAMEGAMETYPE_UPDATE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateGameGametypeRequestDTO dto) {
        GameGametypeServiceDTO serviceDTO = new GameGametypeServiceDTO(dto.game, dto.gametype, dto.tag);
        GameGametype gameGametype = gameGametypeCrudService.update(id, serviceDTO);
        return ResponseEntity.ok(gameGametype);
    }

    @CheckToken
    @CheckRoles(Role.GAMEGAMETYPE_DELETE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        gameGametypeCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
