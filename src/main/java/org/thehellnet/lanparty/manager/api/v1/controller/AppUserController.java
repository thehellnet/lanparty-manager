package org.thehellnet.lanparty.manager.api.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.exception.appuser.AppUserException;
import org.thehellnet.lanparty.manager.exception.appuser.AppUserNotFoundException;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.dto.request.appuser.AppUserLoginRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.crud.create.AppUserCreateCrudRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.response.appuser.AppUserLoginResponseDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.service.AppUserService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(
        path = "/api/v1/public/appUser",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AppUserController {

    private static final Logger logger = LoggerFactory.getLogger(AppUserController.class);

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody AppUserCreateCrudRequestDTO dto) {
        AppUser user;
        try {
            user = appUserService.create(dto.getEmail(), dto.getPassword(), dto.getName());
        } catch (AppUserException e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.created(URI.create("")).body(user);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(method = RequestMethod.GET, path = "{id}")
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        AppUser user = appUserService.get(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<AppUser> appUsers = appUserService.getAll();
        return ResponseEntity.ok(appUsers);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(method = RequestMethod.PATCH, path = "{id}")
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id,
                                 @RequestParam(required = false) String name,
                                 @RequestParam(required = false) String password,
                                 @RequestParam(required = false) String[] appUserRoles) {
        if ((name == null || name.strip().length() == 0)
                && (password == null || password.strip().length() == 0)
                && (appUserRoles == null || appUserRoles.length == 0)) {
            return ResponseEntity.noContent().build();
        }

        AppUser user;

        try {
            user = appUserService.update(id, name, password, appUserRoles);
        } catch (AppUserNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(method = RequestMethod.DELETE, path = "{id}")
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        try {
            appUserService.delete(id);
        } catch (AppUserNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody AppUserLoginRequestDTO dto) {
        AppUser appUser = appUserService.findByEmailAndPassword(dto.email, dto.password);
        if (appUser == null
                || !appUserService.hasAllRoles(appUser, Role.LOGIN)) {
            return ResponseEntity.notFound().build();
        }

        AppUserToken appUserToken = appUserService.newToken(appUser);

        AppUserLoginResponseDTO body = new AppUserLoginResponseDTO()
                .setToken(appUserToken.getToken())
                .setExpiration(appUserToken.getExpirationDateTime());

        return ResponseEntity.ok(body);
    }
}
