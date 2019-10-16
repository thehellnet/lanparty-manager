package org.thehellnet.lanparty.manager.api.v1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thehellnet.lanparty.manager.service.TournamentService;

@Controller
@RequestMapping(path = "/api/v1/public/tournament")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

}
