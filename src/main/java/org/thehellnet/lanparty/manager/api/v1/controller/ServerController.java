package org.thehellnet.lanparty.manager.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thehellnet.lanparty.manager.repository.ServerRepository;

@Controller
@RequestMapping(path = "/api/v1/public/server")
public class ServerController {

    private final ServerRepository serverRepository;

    @Autowired
    public ServerController(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

}
