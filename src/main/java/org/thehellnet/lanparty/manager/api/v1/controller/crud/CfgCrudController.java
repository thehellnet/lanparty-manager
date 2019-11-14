package org.thehellnet.lanparty.manager.api.v1.controller.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Cfg;
import org.thehellnet.lanparty.manager.repository.CfgRepository;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/crud/cfg")
public class CfgCrudController extends AbstractCrudController<Cfg, CfgRepository> {

    private static final Logger logger = LoggerFactory.getLogger(CfgCrudController.class);

    protected CfgCrudController(CfgRepository repository) {
        super(repository);
    }

    @CheckToken
    @CheckRoles(Role.CFG_CREATE)
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody Cfg dto) {
        return createEntity(dto);
    }

    @CheckToken
    @CheckRoles(Role.CFG_READ)
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        return readEntity(id);
    }

    @CheckToken
    @CheckRoles(Role.CFG_READ)
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        return readEntity();
    }

    @CheckToken
    @CheckRoles(Role.CFG_UPDATE)
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody Cfg dto) {
        return updateEntity(id, dto);
    }

    @CheckToken
    @CheckRoles(Role.CFG_DELETE)
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        return deleteEntity(id);
    }
}
