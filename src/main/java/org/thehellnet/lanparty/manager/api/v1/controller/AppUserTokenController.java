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
import org.thehellnet.lanparty.manager.model.dto.request.appusertoken.CreateAppUserTokenRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.appusertoken.UpdateAppUserTokenRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.service.AppUserTokenServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.service.crud.AppUserTokenCrudService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/public/appUserToken")
public class AppUserTokenController {

    private static final Logger logger = LoggerFactory.getLogger(AppUserTokenController.class);

    private final AppUserTokenCrudService appUserTokenCrudService;

    @Autowired
    public AppUserTokenController(AppUserTokenCrudService appUserTokenCrudService) {
        this.appUserTokenCrudService = appUserTokenCrudService;
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateAppUserTokenRequestDTO dto) {
        AppUserTokenServiceDTO serviceDTO = new AppUserTokenServiceDTO(dto.token,dto.appUser);
        AppUserToken appUserToken = appUserTokenCrudService.create(serviceDTO);
        return ResponseEntity.created(URI.create("")).body(appUserToken);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        AppUserToken appUserToken = appUserTokenCrudService.read(id);
        return ResponseEntity.ok(appUserToken);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<AppUserToken> appUserTokens = appUserTokenCrudService.readAll();
        return ResponseEntity.ok(appUserTokens);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateAppUserTokenRequestDTO dto) {
        AppUserTokenServiceDTO serviceDTO = new AppUserTokenServiceDTO(dto.token,dto.appUser);
        AppUserToken appUserToken = appUserTokenCrudService.update(id, serviceDTO);
        return ResponseEntity.ok(appUserToken);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        appUserTokenCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
