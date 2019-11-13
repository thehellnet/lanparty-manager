package org.thehellnet.lanparty.manager.api.v1.controller.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.repository.AppUserTokenRepository;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/crud/appUserToken")
public class AppUserTokenCrudController extends AbstractCrudController<AppUserToken, AppUserTokenRepository> {

    private static final Logger logger = LoggerFactory.getLogger(AppUserTokenCrudController.class);

    protected AppUserTokenCrudController(AppUserTokenRepository repository) {
        super(repository);
    }

    @CheckToken
    @CheckRoles(Role.APPUSERTOKEN_CREATE)
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody AppUserToken dto) {
        return createEntity(dto);
    }

    @CheckToken
    @CheckRoles(Role.APPUSERTOKEN_READ)
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        return readEntity(id);
    }

    @CheckToken
    @CheckRoles(Role.APPUSERTOKEN_READ)
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        return readEntity();
    }

    @CheckToken
    @CheckRoles(Role.APPUSERTOKEN_UPDATE)
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody AppUserToken dto) {
        return updateEntity(id, dto);
    }

    @CheckToken
    @CheckRoles(Role.APPUSERTOKEN_DELETE)
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        return deleteEntity(id);
    }

    @Override
    protected void updateImpl(AppUserToken entity, AppUserToken dto) {
        entity.setToken(dto.getToken());
        entity.setAppUser(dto.getAppUser());
    }

    @Override
    protected void updateImplElse(Long id, AppUserToken dto) {
        dto.setId(id);
    }
}
