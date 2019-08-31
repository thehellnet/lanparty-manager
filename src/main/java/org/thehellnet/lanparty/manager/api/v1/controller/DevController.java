package org.thehellnet.lanparty.manager.api.v1.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thehellnet.lanparty.manager.model.dto.JsonResponse;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.Seat;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.repository.GameRepository;
import org.thehellnet.lanparty.manager.repository.SeatRepository;
import org.thehellnet.lanparty.manager.repository.TournamentRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/dev")
public class DevController {

    private final GameRepository gameRepository;
    private final TournamentRepository tournamentRepository;
    private final SeatRepository seatRepository;

    public DevController(GameRepository gameRepository, TournamentRepository tournamentRepository, SeatRepository seatRepository) {
        this.gameRepository = gameRepository;
        this.tournamentRepository = tournamentRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    @RequestMapping(
            path = "/generateDemoData",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public JsonResponse generateTournament() {
        List<String> data = new ArrayList<>();

        Game game = gameRepository.findByTag("cod4");
        data.add(game.toString());

        Tournament tournament = new Tournament();
        tournament.setName("Test tournament");
        tournament.setGame(game);
        tournament = tournamentRepository.save(tournament);
        data.add(tournament.toString());

        Seat seat = new Seat();
        seat.setName("Test");
        seat.setIpAddress("0.0.0.0");
        seat.setTournament(tournament);
        seat = seatRepository.save(seat);
        data.add(seat.toString());

        return JsonResponse.getInstance(data);
    }
}
