package org.thehellnet.lanparty.manager.api.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thehellnet.lanparty.manager.model.dto.request.match.AssignServerMatchMatchRequestDTO;
import org.thehellnet.lanparty.manager.service.MatchService;

import javax.validation.Valid;

@RestController
@PreAuthorize("isFullyAuthenticated()")
@RequestMapping(
        path = "/api/public/v1/match",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class MatchController {

    private static final Logger logger = LoggerFactory.getLogger(MatchController.class);

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping(path = "/assign-server-match")
    public ResponseEntity<Object> assignServerMatch(@RequestBody @Valid AssignServerMatchMatchRequestDTO requestDTO) {
        logger.info("assignServerMatch {} to match {}", requestDTO.serverMatchId, requestDTO.matchId);
        matchService.assignServerMatch(requestDTO.matchId, requestDTO.serverMatchId);
        return ResponseEntity.noContent().build();
    }
}
