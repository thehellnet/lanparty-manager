package org.thehellnet.lanparty.manager.api.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.thehellnet.lanparty.manager.service.LoginService;
import org.thehellnet.lanparty.manager.service.crud.AppUserCrudService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/public/appUser")
public class AppUserController {

    private static final Logger logger = LoggerFactory.getLogger(AppUserController.class);

    private final AppUserCrudService appUserCrudService;
    private final LoginService loginService;

    public AppUserController(AppUserCrudService appUserCrudService, LoginService loginService) {
        this.appUserCrudService = appUserCrudService;
        this.loginService = loginService;
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_CREATE)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateAppUserRequestDTO dto) {
        AppUserServiceDTO serviceDTO = new AppUserServiceDTO(dto.email, dto.name, dto.password, null, dto.barcode);
        AppUser user = appUserCrudService.create(serviceDTO);
        return ResponseEntity.created(URI.create("")).body(user);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_READ)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        AppUser user = appUserCrudService.read(id);
        return ResponseEntity.ok(user);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_READ)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<AppUser> appUsers = appUserCrudService.readAll();
        return ResponseEntity.ok(appUsers);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_UPDATE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateAppUserRequestDTO dto) {
        AppUserServiceDTO serviceDTO = new AppUserServiceDTO(null, dto.name, dto.password, dto.appUserRoles, dto.barcode);
        AppUser user = appUserCrudService.update(id, serviceDTO);
        return ResponseEntity.ok(user);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_DELETE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        appUserCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(
            path = "/login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity login(@RequestBody LoginAppUserRequestDTO dto) {
        AppUser appUser = loginService.findByEmailAndPassword(dto.email, dto.password);
        AppUserToken appUserToken = loginService.newToken(appUser);

        LoginAppUserResponseDTO body = new LoginAppUserResponseDTO();
        body.id = appUserToken.getId();
        body.token = appUserToken.getToken();
        body.expiration = appUserToken.getExpirationDateTime();
        return ResponseEntity.ok(body);
    }

    @CheckToken
    @CheckRoles(Role.ACTION_LOGIN)
    @RequestMapping(
            path = "/isTokenValid",
            method = RequestMethod.GET
    )
    public ResponseEntity isTokenValid(HttpServletRequest request, AppUser appUser) {
        return ResponseEntity.noContent().build();
    }
}
