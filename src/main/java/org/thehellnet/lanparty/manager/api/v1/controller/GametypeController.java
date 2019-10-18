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
import org.thehellnet.lanparty.manager.model.dto.request.gametype.CreateGametypeRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.gametype.UpdateGametypeRequestDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Gametype;
import org.thehellnet.lanparty.manager.service.GametypeService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/public/gametype")
public class GametypeController {

    private static final Logger logger = LoggerFactory.getLogger(GametypeController.class);

    private final GametypeService gametypeService;

    @Autowired
    public GametypeController(GametypeService gametypeService) {
        this.gametypeService = gametypeService;
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateGametypeRequestDTO dto) {
        Gametype gametype = gametypeService.create(dto.name);
        return ResponseEntity.created(URI.create("")).body(gametype);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        Gametype gametype = gametypeService.read(id);
        return ResponseEntity.ok(gametype);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<Gametype> gametypes = gametypeService.readAll();
        return ResponseEntity.ok(gametypes);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateGametypeRequestDTO dto) {
        Gametype gametype = gametypeService.update(id, dto.name);
        return ResponseEntity.ok(gametype);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        gametypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
