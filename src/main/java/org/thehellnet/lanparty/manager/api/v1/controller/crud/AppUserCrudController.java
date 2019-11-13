package org.thehellnet.lanparty.manager.api.v1.controller.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.repository.AppUserRepository;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/crud/appUser")
public class AppUserCrudController extends AbstractCrudController<AppUser, AppUserRepository> {

    private static final Logger logger = LoggerFactory.getLogger(AppUserCrudController.class);

    public AppUserCrudController(AppUserRepository repository) {
        super(repository);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_CREATE)
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody AppUser dto) {
        return createEntity(dto);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_READ)
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        return readEntity(id);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_READ)
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        return readEntity();
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_UPDATE)
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody AppUser dto) {
        return updateEntity(id, dto);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_DELETE)
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        return deleteEntity(id);
    }

    @Override
    protected void updateImpl(AppUser entity, AppUser dto) {
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setName(dto.getName());
        entity.setBarcode(dto.getBarcode());
    }

    @Override
    protected void updateImplElse(Long id, AppUser dto) {
        dto.setId(id);
    }
}
