package org.thehellnet.lanparty.manager.api.v1.controller;

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
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Match;
import org.thehellnet.lanparty.manager.service.MatchService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/public/match")
public class MatchController {

    private static final Logger logger = LoggerFactory.getLogger(MatchController.class);

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(HttpServletRequest request, AppUser appUser, @RequestBody CreateMatchRequestDTO dto) {
        Match match = matchService.create(dto.name, dto.tournament, dto.server, dto.gameMap, dto.gametype, dto.localTeam, dto.guestTeam);
        return ResponseEntity.created(URI.create("")).body(match);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        Match match = matchService.get(id);
        return ResponseEntity.ok(match);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity read(HttpServletRequest request, AppUser appUser) {
        List<Match> matchs = matchService.getAll();
        return ResponseEntity.ok(matchs);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity update(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id, @RequestBody UpdateMatchRequestDTO dto) {
        Match match = matchService.update(id, dto.name, dto.tournament, dto.server, dto.gameMap, dto.gametype, dto.localTeam, dto.guestTeam);
        return ResponseEntity.ok(match);
    }

    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @RequestMapping(
            path = "{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity delete(HttpServletRequest request, AppUser appUser, @PathVariable(value = "id") Long id) {
        matchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
