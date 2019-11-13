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
import org.thehellnet.lanparty.manager.model.dto.request.appusertoken.CreateAppUserTokenRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.appusertoken.UpdateAppUserTokenRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.service.AppUserTokenServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.service.crud.AppUserTokenCrudServiceOLD;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/crud/appUserToken")
public class AppUserTokenCrudController {

    private static final Logger logger = LoggerFactory.getLogger(AppUserTokenCrudController.class);

    private final AppUserTokenCrudServiceOLD appUserTokenCrudService;

    @Autowired
    public AppUserTokenCrudController(AppUserTokenCrudServiceOLD appUserTokenCrudService) {
        this.appUserTokenCrudService = appUserTokenCrudService;
    }

    @CheckToken
    @CheckRoles(Role.APPUSERTOKEN_CREATE)
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
    @CheckRoles(Role.APPUSERTOKEN_READ)
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
    @CheckRoles(Role.APPUSERTOKEN_READ)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<AppUserToken> appUserTokens = appUserTokenCrudService.readAll();
        return ResponseEntity.ok(appUserTokens);
    }

    @CheckToken
    @CheckRoles(Role.APPUSERTOKEN_UPDATE)
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
    @CheckRoles(Role.APPUSERTOKEN_DELETE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        appUserTokenCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
