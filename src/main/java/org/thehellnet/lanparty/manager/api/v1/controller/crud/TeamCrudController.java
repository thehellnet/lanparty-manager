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
import org.thehellnet.lanparty.manager.model.dto.request.team.CreateTeamRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.team.UpdateTeamRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.service.TeamServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Team;
import org.thehellnet.lanparty.manager.service.crud.TeamCrudService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/crud/team")
public class TeamCrudController {

    private static final Logger logger = LoggerFactory.getLogger(TeamCrudController.class);

    private final TeamCrudService teamCrudService;

    @Autowired
    public TeamCrudController(TeamCrudService teamCrudService) {
        this.teamCrudService = teamCrudService;
    }

    @CheckToken
    @CheckRoles(Role.TEAM_CREATE)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateTeamRequestDTO dto) {
        TeamServiceDTO serviceDTO = new TeamServiceDTO(dto.name, dto.tournament);
        Team team = teamCrudService.create(serviceDTO);
        return ResponseEntity.created(URI.create("")).body(team);
    }

    @CheckToken
    @CheckRoles(Role.TEAM_READ)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        Team team = teamCrudService.read(id);
        return ResponseEntity.ok(team);
    }

    @CheckToken
    @CheckRoles(Role.TEAM_READ)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<Team> teams = teamCrudService.readAll();
        return ResponseEntity.ok(teams);
    }

    @CheckToken
    @CheckRoles(Role.TEAM_UPDATE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateTeamRequestDTO dto) {
        TeamServiceDTO serviceDTO = new TeamServiceDTO(dto.name, dto.tournament);
        Team team = teamCrudService.update(id, serviceDTO);
        return ResponseEntity.ok(team);
    }

    @CheckToken
    @CheckRoles(Role.TEAM_DELETE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        teamCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
