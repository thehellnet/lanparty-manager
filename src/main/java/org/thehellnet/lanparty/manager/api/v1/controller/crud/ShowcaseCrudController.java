package org.thehellnet.lanparty.manager.api.v1.controller.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.dto.request.showcase.CreateShowcaseRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.showcase.UpdateShowcaseRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.service.ShowcaseServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Showcase;
import org.thehellnet.lanparty.manager.service.crud.ShowcaseCrudService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/crud/showcase")
public class ShowcaseCrudController {

    private static final Logger logger = LoggerFactory.getLogger(TeamCrudController.class);

    private final ShowcaseCrudService showcaseCrudService;

    public ShowcaseCrudController(ShowcaseCrudService showcaseCrudService) {
        this.showcaseCrudService = showcaseCrudService;
    }

    @CheckToken
    @CheckRoles(Role.SHOWCASE_CREATE)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateShowcaseRequestDTO dto) {
        ShowcaseServiceDTO serviceDTO = new ShowcaseServiceDTO(dto.tag, dto.name, dto.mode, dto.tournament, dto.match, dto.lastAddress, dto.lastContact);
        Showcase showcase = showcaseCrudService.create(serviceDTO);
        return ResponseEntity.created(URI.create("")).body(showcase);
    }

    @CheckToken
    @CheckRoles(Role.SHOWCASE_READ)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        Showcase showcase = showcaseCrudService.read(id);
        return ResponseEntity.ok(showcase);
    }

    @CheckToken
    @CheckRoles(Role.SHOWCASE_READ)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<Showcase> showcases = showcaseCrudService.readAll();
        return ResponseEntity.ok(showcases);
    }

    @CheckToken
    @CheckRoles(Role.SHOWCASE_UPDATE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateShowcaseRequestDTO dto) {
        ShowcaseServiceDTO serviceDTO = new ShowcaseServiceDTO(dto.tag, dto.name, dto.mode, dto.tournament, dto.match, dto.lastAddress, dto.lastContact);
        Showcase showcase = showcaseCrudService.update(id, serviceDTO);
        return ResponseEntity.ok(showcase);
    }

    @CheckToken
    @CheckRoles(Role.SHOWCASE_DELETE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        showcaseCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
