package org.thehellnet.lanparty.manager.api.v1.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thehellnet.lanparty.manager.model.dto.JsonResponse;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.GameRepository;
import org.thehellnet.lanparty.manager.repository.TournamentRepository;

@Controller
@RequestMapping(path = "/dev")
public class DevController {

    private final GameRepository gameRepository;
    private final TournamentRepository tournamentRepository;

    public DevController(GameRepository gameRepository, TournamentRepository tournamentRepository) {
        this.gameRepository = gameRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional
    @RequestMapping(
            path = "/generateDemoData",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public JsonResponse generateTournament() {
        Game game = gameRepository.findByTag("cod4");

        Tournament tournament = new Tournament();
        tournament.setName("Test tournament");
        tournament.setGame(game);
        tournament = tournamentRepository.save(tournament);

        return JsonResponse.getInstance(tournament.getName());
    }
}
