package org.thehellnet.lanparty.manager.api.v1.controller;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thehellnet.lanparty.manager.model.constant.PaneMode;
import org.thehellnet.lanparty.manager.model.constant.TournamentMode;
import org.thehellnet.lanparty.manager.model.persistence.*;
import org.thehellnet.lanparty.manager.repository.*;
import org.thehellnet.lanparty.manager.service.TournamentService;
import org.thehellnet.utility.PasswordUtility;

@Controller
@RequestMapping(path = "/dev")
@Transactional
public class DevController {

    private static final String APPUSER_1_EMAIL = "user1@domain.tdl";
    private static final String APPUSER_1_PASSWORD = "password1";

    private static final String APPUSER_2_EMAIL = "user2@domain.tdl";
    private static final String APPUSER_2_PASSWORD = "password2";

    private static final String GAME_TAG_COD4 = "cod4";
    private static final String GAME_TAG_LOL = "lol";

    private static final String TOURNAMENT_1_NAME = "Call of Duty 4 tournament";
    private static final String TOURNAMENT_2_NAME = "League of Legends - 5v5";
    private static final String TOURNAMENT_3_NAME = "League of Legends - 1v1";

    private static final String SEAT_1_NAME = "Test seat 1";
    private static final String SEAT_1_ADDRESS = "1.2.3.4";

    private static final String SEAT_2_NAME = "Test seat 2";
    private static final String SEAT_2_ADDRESS = "2.3.4.5";

    private static final String TEAM_1_NAME = "Test team 1";
    private static final String TEAM_2_NAME = "Test team 2";
    private static final String TEAM_3_NAME = "Test team 3";
    private static final String TEAM_4_NAME = "Test team 4";

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

    private static final String MATCH_NAME = "Match";

    private static final String SHOWCASE_1_NAME = "Showcase matches";
    private static final String SHOWCASE_1_TAG = "showcase1";

    private static final String SHOWCASE_2_NAME = "Showcase scores";
    private static final String SHOWCASE_2_TAG = "showcase2";

    private static final String SHOWCASE_3_NAME = "Showcase single match";
    private static final String SHOWCASE_3_TAG = "showcase3";

    private static final String SERVER_TAG = "testserver";
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 28960;
    private static final String SERVER_LOGFILE_PATH = "${user.home}/.callofduty4/main/games_mp.log";
    private static final String SERVER_RCON_PASSWORD = "test";

    private final AppUserRepository appUserRepository;
    private final GameRepository gameRepository;
    private final TournamentRepository tournamentRepository;
    private final SeatRepository seatRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final ShowcaseRepository showcaseRepository;
    private final ServerRepository serverRepository;
    private final PaneRepository paneRepository;

    private final TournamentService tournamentService;

    public DevController(AppUserRepository appUserRepository,
                         GameRepository gameRepository,
                         TournamentRepository tournamentRepository,
                         SeatRepository seatRepository,
                         TeamRepository teamRepository,
                         PlayerRepository playerRepository,
                         MatchRepository matchRepository,
                         ShowcaseRepository showcaseRepository,
                         ServerRepository serverRepository,
                         PaneRepository paneRepository, TournamentService tournamentService) {
        this.appUserRepository = appUserRepository;
        this.gameRepository = gameRepository;
        this.tournamentRepository = tournamentRepository;
        this.seatRepository = seatRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.showcaseRepository = showcaseRepository;
        this.serverRepository = serverRepository;
        this.paneRepository = paneRepository;
        this.tournamentService = tournamentService;
    }

    @Transactional
    @GetMapping(
            path = "/generateDemoData",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> generateDemoData() {
        JSONObject data = new JSONObject();

        AppUser appUser1 = prepareAppUser(APPUSER_1_EMAIL, APPUSER_1_PASSWORD);
        data.put("appUser1", appUser1);

        AppUser appUser2 = prepareAppUser(APPUSER_2_EMAIL, APPUSER_2_PASSWORD);
        data.put("appUser2", appUser2);

        Game cod4Game = gameRepository.findByTag(GAME_TAG_COD4);
        data.put("cod4Game", cod4Game);

        Game lolGame = gameRepository.findByTag(GAME_TAG_LOL);
        data.put("lolGame", lolGame);

        Tournament tournament1 = prepareTournament(TOURNAMENT_1_NAME, cod4Game, TOURNAMENT_CFG);
        data.put("tournament1", tournament1);

        Tournament tournament2 = prepareTournament(TOURNAMENT_2_NAME, lolGame, null);
        data.put("tournament2", tournament2);

        Tournament tournament3 = prepareTournament(TOURNAMENT_3_NAME, lolGame, null);
        data.put("tournament3", tournament3);

        Seat seat1 = prepareSeat(tournament1, SEAT_1_ADDRESS, SEAT_1_NAME);
        data.put("seat1", seat1);

        Seat seat2 = prepareSeat(tournament1, SEAT_2_ADDRESS, SEAT_2_NAME);
        data.put("seat2", seat2);

        Team team1 = prepareTeam(TEAM_1_NAME, tournament1);
        data.put("team1", team1);

        Team team2 = prepareTeam(TEAM_2_NAME, tournament1);
        data.put("team2", team2);

        Team team3 = prepareTeam(TEAM_3_NAME, tournament1);
        data.put("team3", team3);

        Team team4 = prepareTeam(TEAM_4_NAME, tournament1);
        data.put("team4", team4);

        Player player1 = preparePlayer(appUser1, team1, PLAYER_1_NICKNAME);
        data.put("player1", player1);

        Player player2 = preparePlayer(appUser2, team2, PLAYER_2_NICKNAME);
        data.put("player2", player2);

        Showcase showcase1 = prepareShowcase(SHOWCASE_1_TAG, SHOWCASE_1_NAME);
        data.put("showcase1", showcase1);

        Showcase showcase2 = prepareShowcase(SHOWCASE_2_TAG, SHOWCASE_2_NAME);
        data.put("showcase2", showcase2);

        Showcase showcase3 = prepareShowcase(SHOWCASE_3_TAG, SHOWCASE_3_NAME);
        data.put("showcase3", showcase3);

        Pane pane1 = preparePane(showcase1, PaneMode.MATCHES, tournament1);
        data.put("pane1", pane1);

        Pane pane2 = preparePane(showcase1, PaneMode.SCORES, tournament1);
        data.put("pane1", pane2);

        Server server = prepareServer(SERVER_TAG, cod4Game, SERVER_ADDRESS, SERVER_PORT, SERVER_LOGFILE_PATH, SERVER_RCON_PASSWORD);
        data.put("server", server);

        tournament1.setMode(TournamentMode.TEAMS_DOUBLE_ROUND_ROBIN_ELIMINATION);
        tournament1 = tournamentRepository.save(tournament1);
        tournamentService.generateMatchesTeamsDoubleRoundRobinElimination(tournament1);

        tournament1 = tournamentRepository.findById(tournament1.getId()).orElseThrow();
        data.put("tournament1", tournament1);

        return ResponseEntity.ok(data.toString());
    }

    private AppUser prepareAppUser(String email, String password) {
        AppUser appUser = appUserRepository.findByEnabledTrueAndEmail(email);
        if (appUser == null) {
            appUser = new AppUser();
        }
        appUser.setEmail(email);
        appUser.setPassword(PasswordUtility.newInstance().hash(password));
        appUser.setEnabled(true);
        return appUserRepository.save(appUser);
    }

    private Tournament prepareTournament(String name, Game game, String cfg) {
        Tournament tournament = tournamentRepository.findByName(name);
        if (tournament == null) {
            tournament = new Tournament();
        }
        tournament.setName(name);
        tournament.setGame(game);
        tournament.setCfg(cfg);

        DateTime now = DateTime.now();
        tournament.setStartRegistrationTs(now);
        tournament.setEndRegistrationTs(now.plusHours(1));
        tournament.setStartTs(now.plusHours(1).plusMinutes(15));
        tournament.setEndTs(now.plusHours(1).plusMinutes(45));

        return tournamentRepository.save(tournament);
    }

    private Seat prepareSeat(Tournament tournament, String address, String name) {
        Seat seat = seatRepository.findByNameAndTournament(name, tournament);
        if (seat == null) {
            seat = new Seat(name, address, tournament);
        }
        return seatRepository.save(seat);
    }

    private Team prepareTeam(String name, Tournament tournament) {
        Team team = teamRepository.findByName(name);
        if (team == null) {
            team = new Team();
        }
        team.setName(name);
        team.setTournament(tournament);
        return teamRepository.save(team);
    }

    private Player preparePlayer(AppUser appUser, Team team, String playerNickname) {
        Player player = playerRepository.findByNickname(playerNickname);
        if (player == null) {
            player = new Player();
        }
        player.setNickname(playerNickname);
        player.setAppUser(appUser);
        player.setTeam(team);
        return playerRepository.save(player);
    }

    private Match prepareMatch(String name, Tournament tournament, Team localTeam, Team guestTeam) {
        Match match = matchRepository.findByName(name);
        if (match == null) {
            match = new Match();
        }
        match.setName(name);
        match.setTournament(tournament);
        match.setLocalTeam(localTeam);
        match.setGuestTeam(guestTeam);
        return matchRepository.save(match);
    }

    private Showcase prepareShowcase(String tag, String name) {
        Showcase showcase = showcaseRepository.findByTag(tag);
        if (showcase == null) {
            showcase = new Showcase();
        }
        showcase.setTag(tag);
        showcase.setName(name);
        return showcaseRepository.save(showcase);
    }

    private Server prepareServer(String tag, Game game, String address, int port, String logFile, String rconPassword) {
        Server server = serverRepository.findByAddressAndPort(SERVER_ADDRESS, SERVER_PORT);
        if (server == null) {
            server = new Server();
        }
        server.setTag(tag);
        server.setGame(game);
        server.setAddress(address);
        server.setPort(port);
        server.setLogParsingEnabled(logFile != null);
        server.setRconPassword(rconPassword);
        return serverRepository.save(server);
    }

    private Pane preparePane(Showcase showcase, PaneMode mode, Tournament tournament) {
        Pane pane = paneRepository.findByShowcaseAndModeAndTournament(showcase, mode, tournament);
        if (pane == null) {
            pane = new Pane(showcase, mode, tournament);
        }
        pane.updateName();
        return paneRepository.save(pane);
    }
}
