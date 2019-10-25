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
import org.thehellnet.lanparty.manager.model.dto.request.cfg.CreateCfgRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.cfg.UpdateCfgRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.service.CfgServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Cfg;
import org.thehellnet.lanparty.manager.service.crud.CfgCrudService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/crud/cfg")
public class CfgCrudController {

    private static final Logger logger = LoggerFactory.getLogger(CfgCrudController.class);

    private final CfgCrudService cfgCrudService;

    @Autowired
    public CfgCrudController(CfgCrudService cfgCrudService) {
        this.cfgCrudService = cfgCrudService;
    }

    @CheckToken
    @CheckRoles(Role.CFG_CREATE)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateCfgRequestDTO dto) {
        CfgServiceDTO serviceDTO = new CfgServiceDTO(dto.player, dto.game, dto.cfgContent);
        Cfg cfg = cfgCrudService.create(serviceDTO);
        return ResponseEntity.created(URI.create("")).body(cfg);
    }

    @CheckToken
    @CheckRoles(Role.CFG_READ)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        Cfg cfg = cfgCrudService.read(id);
        return ResponseEntity.ok(cfg);
    }

    @CheckToken
    @CheckRoles(Role.CFG_READ)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<Cfg> cfgs = cfgCrudService.readAll();
        return ResponseEntity.ok(cfgs);
    }

    @CheckToken
    @CheckRoles(Role.CFG_UPDATE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateCfgRequestDTO dto) {
        CfgServiceDTO serviceDTO = new CfgServiceDTO(dto.player, dto.game, dto.cfgContent);
        Cfg cfg = cfgCrudService.update(id, serviceDTO);
        return ResponseEntity.ok(cfg);
    }

    @CheckToken
    @CheckRoles(Role.CFG_DELETE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        cfgCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
