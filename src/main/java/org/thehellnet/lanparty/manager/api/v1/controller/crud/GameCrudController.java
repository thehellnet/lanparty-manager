package org.thehellnet.lanparty.manager.api.v1.controller.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.repository.GameRepository;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/crud/game")
public class GameCrudController extends AbstractCrudController<Game, GameRepository> {

    private static final Logger logger = LoggerFactory.getLogger(GameCrudController.class);

    protected GameCrudController(GameRepository repository) {
        super(repository);
    }

    @CheckToken
    @CheckRoles(Role.GAME_CREATE)
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody Game dto) {
        return createEntity(dto);
    }

    @CheckToken
    @CheckRoles(Role.GAME_READ)
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        return readEntity(id);
    }

    @CheckToken
    @CheckRoles(Role.GAME_READ)
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        return readEntity();
    }

    @CheckToken
    @CheckRoles(Role.GAME_UPDATE)
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody Game dto) {
        return updateEntity(id, dto);
    }

    @CheckToken
    @CheckRoles(Role.GAME_DELETE)
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        return deleteEntity(id);
    }
}
