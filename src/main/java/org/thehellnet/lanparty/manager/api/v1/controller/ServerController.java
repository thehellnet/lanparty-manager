package org.thehellnet.lanparty.manager.api.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thehellnet.lanparty.manager.service.ServerService;

@RestController
@PreAuthorize("isFullyAuthenticated()")
@RequestMapping(
        path = "/api/public/v1/server",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ServerController {

    private static final Logger logger = LoggerFactory.getLogger(ServerController.class);

    private final ServerService serverService;

    @Autowired
    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping(path = "/map-restart/{serverId}")
    public ResponseEntity<Object> mapRestart(@PathVariable(name = "serverId") Long serverId) {
        logger.info("Map Restart on server {}", serverId);
        serverService.mapRestart(serverId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/fast-restart/{serverId}")
    public ResponseEntity<Object> fastRestart(@PathVariable(name = "serverId") Long serverId) {
        logger.info("Fast Restart on server {}", serverId);
        serverService.fastRestart(serverId);
        return ResponseEntity.noContent().build();
    }

}
