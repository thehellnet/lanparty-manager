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
import org.thehellnet.lanparty.manager.model.dto.request.appuser.CreateAppUserRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.appuser.LoginAppUserRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.appuser.UpdateAppUserRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.response.appuser.LoginAppUserResponseDTO;
import org.thehellnet.lanparty.manager.model.dto.service.AppUserServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.service.crud.AppUserService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/public/appUser")
public class AppUserController {

    private static final Logger logger = LoggerFactory.getLogger(AppUserController.class);

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateAppUserRequestDTO dto) {
        AppUserServiceDTO serviceDTO = new AppUserServiceDTO(dto.email, dto.name, dto.password, null, dto.barcode);
        AppUser user = appUserService.create(serviceDTO);
        return ResponseEntity.created(URI.create("")).body(user);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        AppUser user = appUserService.read(id);
        return ResponseEntity.ok(user);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<AppUser> appUsers = appUserService.readAll();
        return ResponseEntity.ok(appUsers);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateAppUserRequestDTO dto) {
        AppUserServiceDTO serviceDTO = new AppUserServiceDTO(null, dto.name, dto.password, dto.appUserRoles, dto.barcode);
        AppUser user = appUserService.update(id, serviceDTO);
        return ResponseEntity.ok(user);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        appUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(
            path = "/login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity login(@RequestBody LoginAppUserRequestDTO dto) {
        AppUser appUser = appUserService.findByEmailAndPassword(dto.email, dto.password);
        AppUserToken appUserToken = appUserService.newToken(appUser);

        LoginAppUserResponseDTO body = new LoginAppUserResponseDTO();
        body.id = appUserToken.getId();
        body.token = appUserToken.getToken();
        body.expiration = appUserToken.getExpirationDateTime();
        return ResponseEntity.ok(body);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(
            path = "/isTokenValid",
            method = RequestMethod.GET
    )
    public ResponseEntity isTokenValid(HttpServletRequest request, AppUser appUser) {
        return ResponseEntity.noContent().build();
    }
}
