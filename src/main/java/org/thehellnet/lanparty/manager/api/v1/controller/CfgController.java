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
import org.thehellnet.lanparty.manager.model.dto.request.cfg.CreateCfgRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.cfg.UpdateCfgRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.service.CfgServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Cfg;
import org.thehellnet.lanparty.manager.service.crud.CfgService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/public/cfg")
public class CfgController {

    private static final Logger logger = LoggerFactory.getLogger(CfgController.class);

    private final CfgService cfgService;

    @Autowired
    public CfgController(CfgService cfgService) {
        this.cfgService = cfgService;
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateCfgRequestDTO dto) {
        CfgServiceDTO serviceDTO = new CfgServiceDTO(dto.player, dto.game, dto.cfgContent);
        Cfg cfg = cfgService.create(serviceDTO);
        return ResponseEntity.created(URI.create("")).body(cfg);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        Cfg cfg = cfgService.read(id);
        return ResponseEntity.ok(cfg);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<Cfg> cfgs = cfgService.readAll();
        return ResponseEntity.ok(cfgs);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateCfgRequestDTO dto) {
        CfgServiceDTO serviceDTO = new CfgServiceDTO(dto.player, dto.game, dto.cfgContent);
        Cfg cfg = cfgService.update(id, serviceDTO);
        return ResponseEntity.ok(cfg);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        cfgService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
