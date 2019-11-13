package org.thehellnet.lanparty.manager.api.v1.controller.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.service.crud.AppUserCrudService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/crud/appUser")
public class AppUserCrudController extends AbstractCrudController {

    private static final Logger logger = LoggerFactory.getLogger(AppUserCrudController.class);

    private final AppUserCrudService appUserCrudService;

    public AppUserCrudController(AppUserCrudService appUserCrudService) {
        this.appUserCrudService = appUserCrudService;
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_CREATE)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody Map<String, Object> dto) {
        AppUser user = appUserCrudService.create(dto);
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
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody Map<String, Object> dto) {
        AppUser user = appUserCrudService.update(id, dto);
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
}
