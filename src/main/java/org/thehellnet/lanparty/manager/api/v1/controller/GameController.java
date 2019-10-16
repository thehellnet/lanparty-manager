package org.thehellnet.lanparty.manager.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thehellnet.lanparty.manager.service.GameService;

@Controller
@RequestMapping(path = "/api/v1/public/game")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

}
