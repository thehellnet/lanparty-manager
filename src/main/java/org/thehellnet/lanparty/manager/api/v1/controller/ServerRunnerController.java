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
import org.thehellnet.lanparty.manager.runner.ServerRunner;

@RestController
@PreAuthorize("isFullyAuthenticated()")
@RequestMapping(
        path = "/api/public/v1/server-runner",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ServerRunnerController {

    private static final Logger logger = LoggerFactory.getLogger(ServerRunnerController.class);

    private final ServerRunner serverRunner;

    @Autowired
    public ServerRunnerController(ServerRunner serverRunner) {
        this.serverRunner = serverRunner;
    }

    @GetMapping(path = "/start-all")
    public ResponseEntity<Object> startAll() {
        logger.info("Start All ServerRunners");
        serverRunner.startAll();
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/stop-all")
    public ResponseEntity<Object> stopAll() {
        logger.info("Stop All ServerRunners");
        serverRunner.stopAll();
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/restart-all")
    public ResponseEntity<Object> restartAll() {
        logger.info("Restart All ServerRunners");
        serverRunner.restartAll();
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/start/{serverId}")
    public ResponseEntity<Object> start(@PathVariable(name = "serverId") Long serverId) {
        logger.info("Start ServerRunner for server {}", serverId);
        serverRunner.startSingle(serverId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/stop/{serverId}")
    public ResponseEntity<Object> stop(@PathVariable(name = "serverId") Long serverId) {
        logger.info("Stop ServerRunner for server {}", serverId);
        serverRunner.stopSingle(serverId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/restart/{serverId}")
    public ResponseEntity<Object> restart(@PathVariable(name = "serverId") Long serverId) {
        logger.info("Restart ServerRunner for server {}", serverId);
        serverRunner.restartSingle(serverId);
        return ResponseEntity.noContent().build();
    }
}
