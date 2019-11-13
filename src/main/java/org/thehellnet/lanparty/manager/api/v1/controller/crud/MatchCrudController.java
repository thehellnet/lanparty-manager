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
import org.thehellnet.lanparty.manager.model.dto.request.match.CreateMatchRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.match.UpdateMatchRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.service.MatchServiceDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Match;
import org.thehellnet.lanparty.manager.service.crud.MatchCrudServiceOLD;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/crud/match")
public class MatchCrudController {

    private static final Logger logger = LoggerFactory.getLogger(MatchCrudController.class);

    private final MatchCrudServiceOLD matchCrudService;

    @Autowired
    public MatchCrudController(MatchCrudServiceOLD matchCrudService) {
        this.matchCrudService = matchCrudService;
    }

    @CheckToken
    @CheckRoles(Role.MATCH_CREATE)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateMatchRequestDTO dto) {
        MatchServiceDTO serviceDTO = new MatchServiceDTO(dto.name, dto.tournament, dto.server, dto.gameMap, dto.gametype, dto.localTeam, dto.guestTeam);
        Match match = matchCrudService.create(serviceDTO);
        return ResponseEntity.created(URI.create("")).body(match);
    }

    @CheckToken
    @CheckRoles(Role.MATCH_READ)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        Match match = matchCrudService.read(id);
        return ResponseEntity.ok(match);
    }

    @CheckToken
    @CheckRoles(Role.MATCH_READ)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<Match> matchs = matchCrudService.readAll();
        return ResponseEntity.ok(matchs);
    }

    @CheckToken
    @CheckRoles(Role.MATCH_UPDATE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateMatchRequestDTO dto) {
        MatchServiceDTO serviceDTO = new MatchServiceDTO(dto.name, dto.tournament, dto.server, dto.gameMap, dto.gametype, dto.localTeam, dto.guestTeam);
        Match match = matchCrudService.update(id, serviceDTO);
        return ResponseEntity.ok(match);
    }

    @CheckToken
    @CheckRoles(Role.MATCH_DELETE)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        matchCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
