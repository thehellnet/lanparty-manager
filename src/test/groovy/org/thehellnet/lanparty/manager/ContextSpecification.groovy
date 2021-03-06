package org.thehellnet.lanparty.manager

import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.AnnotationConfigWebContextLoader
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import org.thehellnet.lanparty.manager.configuration.*
import org.thehellnet.lanparty.manager.model.constant.Tags
import org.thehellnet.lanparty.manager.model.persistence.*
import org.thehellnet.lanparty.manager.repository.*
import spock.lang.Shared
import spock.lang.Specification

@WebAppConfiguration
@ContextConfiguration(
        loader = AnnotationConfigWebContextLoader,
        classes = [
                ActiveMQConfiguration,
                EmailConfiguration,
                JacksonConfiguration,
                MvcConfiguration,
                PersistenceConfiguration,
                RestConfiguration,
                SecurityConfiguration,
                SpringConfiguration,
                ThreadsConfiguration,
                WebMvcConfiguration,
                WebSocketConfiguration
        ]
)
@Transactional
@Rollback
abstract class ContextSpecification extends Specification {

    protected static final String APPUSER_EMAIL = "email@email.com"
    protected static final String APPUSER_PASSWORD = "password"
    protected static final String APPUSER_NAME = "Name"
    protected static final String APPUSER_NICKNAME = "Nickname"
    protected static final String APPUSER_BARCODE = "0123456789"

    protected static final String APPUSER_EMAIL_NEW = "emailnew@email.com"
    protected static final String APPUSER_PASSWORD_NEW = "password_new"
    protected static final String APPUSER_NAME_NEW = "Name2"
    protected static final String APPUSER_BARCODE_NEW = "9876543210"

    protected static final String APPUSERTOKEN = "0123456789abcdef"

    protected final static String TOURNAMENT_NAME = "Test Tournament"
    protected final static String TOURNAMENT_NAME_2 = "Test Tournament 2"
    protected final static String TOURNAMENT_CFG = "bind TAB \"+scores\"\n" +
            "bind ESCAPE \"togglemenu\"\n" +
            "bind SPACE \"+gostand\"\n" +
            "bind 4 \"+smoke\"\n" +
            "bind H \"say Google\""

    protected final static String GAME_TAG = Tags.GAME_COD4
    protected final static String GAME_TAG_NEW = Tags.GAME_COD2

    protected final static String SEAT_ADDRESS = "1.2.3.4"
    protected final static String SEAT_NAME = "Test seat"
    protected final static String SEAT_ADDRESS_2 = "2.3.4.5"
    protected final static String SEAT_NAME_2 = "Test seat 2"

    protected final static String TEAM_NAME = "Test team"

    protected final static String PLAYER_NICKNAME = "Testplayer"
    protected final static String PLAYER_NICKNAME_NEW = "Testplayer new"

    protected final static String PLAYER_CFG = "bind H \"say Ciao\""

    protected final static String CFG = "unbindall\n" +
            "bind P \"quit\""

    protected final static String CFG_NEW = "unbindall\n" +
            "bind P \"say mosche\""

    protected final static String SERVER_ADDRESS = "4.3.2.1"
    protected final static int SERVER_PORT = 28960
    protected final static String SERVER_TAG = "test"
    protected final static String SERVER_GAME_TAG = Tags.GAME_COD4

    protected static final String GAMETYPE_TAG = "war"
    protected static final String GAMEMAP_TAG = "mp_backlot"

    @Autowired
    protected WebApplicationContext webApplicationContext

    @Autowired
    protected AppUserRepository appUserRepository

    @Autowired
    protected AppUserTokenRepository appUserTokenRepository

    @Autowired
    protected TournamentRepository tournamentRepository

    @Autowired
    protected SeatRepository seatRepository

    @Autowired
    protected GameRepository gameRepository

    @Autowired
    protected PlayerRepository playerRepository

    @Autowired
    protected TeamRepository teamRepository

    @Autowired
    protected CfgRepository cfgRepository

    @Autowired
    protected ServerRepository serverRepository

    @Autowired
    protected ServerMatchRepository serverMatchRepository

    @Autowired
    protected ServerMatchPlayerRepository serverMatchPlayerRepository

    @Autowired
    protected GametypeRepository gametypeRepository

    @Autowired
    protected GameMapRepository gameMapRepository

    @Shared
    private GreenMail greenMail

    @SuppressWarnings("unused")
    def setupSpec() {
        ServerSetup serverSetup = new ServerSetup(2525, "localhost", "smtp");
        greenMail = new GreenMail(serverSetup)
        greenMail.start()
    }

    @SuppressWarnings("unused")
    def cleanupSpec() {
        greenMail.stop()
    }

    protected AppUser createAppUser() {
        AppUser appUser = appUserRepository.findByEnabledTrueAndEmail(APPUSER_EMAIL)
        if (appUser == null) {
            appUser = new AppUser(APPUSER_EMAIL, APPUSER_PASSWORD, APPUSER_NAME, [] as List<Role>, APPUSER_BARCODE)
            appUser = appUserRepository.save(appUser)
        }
        return appUser
    }

    protected Tournament createTournament() {
        Tournament tournament = tournamentRepository.findByName(TOURNAMENT_NAME)
        if (tournament == null) {
            Game game = gameRepository.findByTag(GAME_TAG)
            tournament = new Tournament(name: TOURNAMENT_NAME, game: game, cfg: TOURNAMENT_CFG)
            tournament = tournamentRepository.save(tournament)
        }
        return tournament
    }

    protected Seat createSeat() {
        Seat seat = seatRepository.findByIpAddress(SEAT_ADDRESS)
        if (seat == null) {
            Tournament tournament = createTournament()
            seat = new Seat(name: SEAT_NAME, ipAddress: SEAT_ADDRESS, tournament: tournament)
            seat = seatRepository.save(seat)
        }
        return seat
    }

    protected Seat createSeat2() {
        Seat seat = seatRepository.findByIpAddress(SEAT_ADDRESS_2)
        if (seat == null) {
            Tournament tournament = createTournament()
            seat = new Seat(name: SEAT_NAME_2, ipAddress: SEAT_ADDRESS_2, tournament: tournament)
            seat = seatRepository.save(seat)
        }
        return seat
    }

    protected Team createTeam() {
        Team team = teamRepository.findByName(TEAM_NAME)
        if (team == null) {
            Tournament tournament = createTournament()
            team = new Team(name: TEAM_NAME, tournament: tournament)
            team = teamRepository.save(team)
        }
        return team
    }

    protected Player createPlayer() {
        return createPlayer(PLAYER_NICKNAME)
    }

    protected Player createNewPlayer() {
        return createPlayer(PLAYER_NICKNAME_NEW)
    }

    protected Cfg createCfg() {
        Player player = createPlayer()
        Game game = gameRepository.findByTag(GAME_TAG)
        Cfg cfg = cfgRepository.findByPlayerAndGame(player, game)
        if (cfg == null) {
            cfg = new Cfg(player: player, game: game, cfgContent: PLAYER_CFG)
            cfg = cfgRepository.save(cfg)
        }
        return cfg
    }

    protected Player createPlayer(String playerName) {
        Player player = playerRepository.findByNickname(playerName)
        if (player == null) {
            AppUser appUser = createAppUser()
            Team team = createTeam()
            player = new Player(nickname: PLAYER_NICKNAME, appUser: appUser, team: team)
            player = playerRepository.save(player)
        }
        return player
    }

    protected Server createServer() {
        Server server = serverRepository.findByAddressAndPort(SERVER_ADDRESS, SERVER_PORT);
        if (server == null) {
            Game game = gameRepository.findByTag(SERVER_GAME_TAG)
            server = new Server(SERVER_TAG, game, SERVER_ADDRESS, SERVER_PORT)
            server = serverRepository.save(server)
        }
        return server
    }

    protected boolean "Player is in one seat only"(Player player) {
        return seatRepository.findAllByPlayer(player).size() == 1
    }
}
