package org.thehellnet.lanparty.manager.api.v1.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thehellnet.lanparty.manager.model.dto.JsonResponse;
import org.thehellnet.lanparty.manager.model.persistence.*;
import org.thehellnet.lanparty.manager.repository.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/dev")
public class DevController {

    private static final String TOURNAMENT_CFG = "unbindall\n" +
            "bind TAB \"+scores\"\n" +
            "bind ESCAPE \"togglemenu\"\n" +
            "bind SPACE \"+gostand\"\n" +
            "bind 1 \"weapnext\"\n" +
            "bind 2 \"weapnext\"\n" +
            "bind 4 \"+smoke\"\n" +
            "bind 5 \"+actionslot 3\"\n" +
            "bind 6 \"+actionslot 4\"\n" +
            "bind 7 \"+actionslot 2\"\n" +
            "bind ` \"toggleconsole\"\n" +
            "bind A \"+moveleft\"\n" +
            "bind B \"mp_QuickMessage\"\n" +
            "bind C \"gocrouch\"\n" +
            "bind D \"+moveright\"\n" +
            "bind E \"+leanright\"\n" +
            "bind F \"+activate\"\n" +
            "bind G \"+frag\"\n" +
            "bind N \"+actionslot 1\"\n" +
            "bind Q \"+leanleft\"\n" +
            "bind R \"+reload\"\n" +
            "bind S \"+back\"\n" +
            "bind T \"chatmodepublic\"\n" +
            "bind V \"+melee\"\n" +
            "bind W \"+forward\"\n" +
            "bind Y \"chatmodeteam\"\n" +
            "bind Z \"+talk\"\n" +
            "bind ~ \"toggleconsole\"\n" +
            "bind PAUSE \"toggle cl_paused\"\n" +
            "bind CTRL \"goprone\"\n" +
            "bind SHIFT \"+breath_sprint\"\n" +
            "bind F1 \"vote yes\"\n" +
            "bind F2 \"vote no\"\n" +
            "bind F4 \"+scores\"\n" +
            "bind F12 \"screenshotJPEG\"\n" +
            "bind MOUSE1 \"+attack\"\n" +
            "bind MOUSE2 \"+toggleads_throw\"\n" +
            "bind MOUSE3 \"+frag\"";

    private final GameRepository gameRepository;
    private final TournamentRepository tournamentRepository;
    private final SeatRepository seatRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public DevController(GameRepository gameRepository, TournamentRepository tournamentRepository, SeatRepository seatRepository, TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.tournamentRepository = tournamentRepository;
        this.seatRepository = seatRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
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

        Tournament tournament = new Tournament("Test tournament", game);
        tournament.setCfg(TOURNAMENT_CFG);
        tournament = tournamentRepository.save(tournament);
        data.add(tournament.toString());

        Seat seat = new Seat("Test", "0.0.0.0", tournament);
        seat = seatRepository.save(seat);
        data.add(seat.toString());

        seat = new Seat("PC05", "1.2.3.4", tournament);
        seat = seatRepository.save(seat);
        data.add(seat.toString());

        Team team1 = new Team("team1", tournament);
        team1 = teamRepository.save(team1);
        data.add(team1.toString());

        Team team2 = new Team("team2", tournament);
        team2 = teamRepository.save(team2);
        data.add(team2.toString());

        Player player = new Player("player1", team1);
        player = playerRepository.save(player);
        data.add(player.toString());

        player = new Player("player2", team2);
        player = playerRepository.save(player);
        data.add(player.toString());

        return JsonResponse.getInstance(data);
    }
}
