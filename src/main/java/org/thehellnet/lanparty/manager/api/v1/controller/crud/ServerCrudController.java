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
import org.thehellnet.lanparty.manager.model.dto.request.server.CreateServerRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.server.UpdateServerRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.service.ServerServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.service.crud.ServerCrudServiceOLD;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/crud/server")
public class ServerCrudController {

    private static final Logger logger = LoggerFactory.getLogger(ServerCrudController.class);

    private final ServerCrudServiceOLD serverCrudService;

    @Autowired
    public ServerCrudController(ServerCrudServiceOLD serverCrudService) {
        this.serverCrudService = serverCrudService;
    }

    @CheckToken
    @CheckRoles(Role.SERVER_CREATE)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateServerRequestDTO dto) {
        ServerServiceDTO serviceDTO = new ServerServiceDTO(dto.tag, dto.name, dto.game, dto.address, dto.port, dto.rconPassword, dto.logFile, dto.logParsingEnabled);
        Server server = serverCrudService.create(serviceDTO);
        return ResponseEntity.created(URI.create("")).body(server);
    }

    @CheckToken
    @CheckRoles(Role.SERVER_READ)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        Server server = serverCrudService.read(id);
        return ResponseEntity.ok(server);
    }

    @CheckToken
    @CheckRoles(Role.SERVER_READ)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<Server> servers = serverCrudService.readAll();
        return ResponseEntity.ok(servers);
    }

    @CheckToken
    @CheckRoles(Role.SERVER_UPDATE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateServerRequestDTO dto) {
        ServerServiceDTO serviceDTO = new ServerServiceDTO(dto.tag, dto.name, dto.game, dto.address, dto.port, dto.rconPassword, dto.logFile, dto.logParsingEnabled);
        Server server = serverCrudService.update(id, serviceDTO);
        return ResponseEntity.ok(server);
    }

    @CheckToken
    @CheckRoles(Role.SERVER_DELETE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        serverCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
