package org.thehellnet.lanparty.manager.api.v1.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thehellnet.lanparty.manager.model.persistence.*;
import org.thehellnet.lanparty.manager.repository.*;
import org.thehellnet.utility.PasswordUtility;

@Controller
@RequestMapping(path = "/dev")
public class DevController {

    private static final String APPUSER_1_EMAIL = "user1@domain.tdl";
    private static final String APPUSER_2_EMAIL = "user2@domain.tdl";
    private static final String APPUSER_PASSWORD = PasswordUtility.hash("password");

    private static final String GAME_TAG = "cod4";

    private static final String TOURNAMENT_NAME = "Test tournament";

    private static final String SEAT_1_NAME = "Test seat 1";
    private static final String SEAT_1_ADDRESS = "0.0.0.0";

    private static final String SEAT_2_NAME = "Test seat 2";
    private static final String SEAT_2_ADDRESS = "1.2.3.4";

    private static final String TEAM_1_NAME = "Test team 1";
    private static final String TEAM_2_NAME = "Test team 2";

    private static final String PLAYER_1_NICKNAME = "player1";
    private static final String PLAYER_2_NICKNAME = "player2";

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
            "bind MOUSE3 \"+frag\"\n" +
            "seta sensitivity \"10\"";

    private final AppUserRepository appUserRepository;
    private final GameRepository gameRepository;
    private final TournamentRepository tournamentRepository;
    private final SeatRepository seatRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public DevController(AppUserRepository appUserRepository, GameRepository gameRepository, TournamentRepository tournamentRepository, SeatRepository seatRepository, TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.appUserRepository = appUserRepository;
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
    public ResponseEntity generateTournament() {
        JSONObject data = new JSONObject();

        AppUser appUser1 = appUserRepository.findByEmail(APPUSER_1_EMAIL);
        if (appUser1 == null) {
            appUser1 = new AppUser(APPUSER_1_EMAIL, APPUSER_PASSWORD);
            appUser1 = appUserRepository.save(appUser1);
        }
        data.put("appUser1", appUser1);

        AppUser appUser2 = appUserRepository.findByEmail(APPUSER_2_EMAIL);
        if (appUser2 == null) {
            appUser2 = new AppUser(APPUSER_2_EMAIL, APPUSER_PASSWORD);
            appUser2 = appUserRepository.save(appUser2);
        }
        data.put("appUser2", appUser2);

        Game game = gameRepository.findByTag(GAME_TAG);
        data.put("game", game);

        Tournament tournament = tournamentRepository.findByName(TOURNAMENT_NAME);
        if (tournament == null) {
            tournament = new Tournament(TOURNAMENT_NAME, game);
            tournament.setCfg(TOURNAMENT_CFG);
            tournament = tournamentRepository.save(tournament);
        }
        data.put("tournament", tournament);

        Seat seat1 = seatRepository.findByIpAddress(SEAT_1_ADDRESS);
        if (seat1 == null) {
            seat1 = new Seat(SEAT_1_NAME, SEAT_1_ADDRESS, tournament);
            seat1 = seatRepository.save(seat1);
        }
        data.put("seat1", seat1);

        Seat seat2 = seatRepository.findByIpAddress(SEAT_2_ADDRESS);
        if (seat2 == null) {
            seat2 = new Seat(SEAT_2_NAME, SEAT_2_ADDRESS, tournament);
            seat2 = seatRepository.save(seat2);
        }
        data.put("seat2", seat2);

        Team team1 = teamRepository.findByName(TEAM_1_NAME);
        if (team1 == null) {
            team1 = new Team(TEAM_1_NAME, tournament);
            team1 = teamRepository.save(team1);
        }
        data.put("team1", team1);

        Team team2 = teamRepository.findByName(TEAM_1_NAME);
        if (team2 == null) {
            team2 = new Team(TEAM_2_NAME, tournament);
            team2 = teamRepository.save(team2);
        }
        data.put("team2", team2);

        Player player1 = playerRepository.findByNickname(PLAYER_1_NICKNAME);
        if (player1 == null) {
            player1 = new Player(PLAYER_1_NICKNAME, appUser1, team1);
            player1 = playerRepository.save(player1);
        }
        data.put("player1", player1);

        Player player2 = playerRepository.findByNickname(PLAYER_2_NICKNAME);
        if (player2 == null) {
            player2 = new Player(PLAYER_2_NICKNAME, appUser2, team2);
            player2 = playerRepository.save(player2);
        }
        data.put("player2", player2);

        return ResponseEntity.ok(data.toString());
    }
}
