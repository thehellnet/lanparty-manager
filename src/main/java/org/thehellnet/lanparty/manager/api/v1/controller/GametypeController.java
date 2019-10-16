package org.thehellnet.lanparty.manager.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thehellnet.lanparty.manager.repository.GameRepository;
import org.thehellnet.lanparty.manager.repository.GametypeRepository;

@Controller
@RequestMapping(path = "/api/v1/public/gametype")
public class GametypeController {

    private final GameRepository gameRepository;
    private final GametypeRepository gametypeRepository;

    @Autowired
    public GametypeController(GameRepository gameRepository, GametypeRepository gametypeRepository) {
        this.gameRepository = gameRepository;
        this.gametypeRepository = gametypeRepository;
    }

}
