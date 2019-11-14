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
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.GameGametype;
import org.thehellnet.lanparty.manager.repository.GameGametypeRepository;
import org.thehellnet.lanparty.manager.service.crud.GameGametypeCrudServiceOLD;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/crud/gameGametype")
public class GameGametypeCrudController extends AbstractCrudController<GameGametype, GameGametypeRepository> {

    private static final Logger logger = LoggerFactory.getLogger(GameGametypeCrudController.class);

    protected GameGametypeCrudController(GameGametypeRepository repository) {
        super(repository);
    }

    @CheckToken
    @CheckRoles(Role.GAMEGAMETYPE_CREATE)
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody GameGametype dto) {
        return createEntity(dto);
    }

    @CheckToken
    @CheckRoles(Role.GAMEGAMETYPE_READ)
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        return readEntity(id);
    }

    @CheckToken
    @CheckRoles(Role.GAMEGAMETYPE_READ)
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        return readEntity();
    }

    @CheckToken
    @CheckRoles(Role.GAMEGAMETYPE_UPDATE)
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody GameGametype dto) {
        return updateEntity(id, dto);
    }

    @CheckToken
    @CheckRoles(Role.GAMEGAMETYPE_DELETE)
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        return deleteEntity(id);
    }
}
