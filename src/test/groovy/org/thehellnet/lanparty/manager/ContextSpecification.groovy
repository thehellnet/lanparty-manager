package org.thehellnet.lanparty.manager

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.AnnotationConfigWebContextLoader
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import org.thehellnet.lanparty.manager.configuration.JacksonConfiguration
import org.thehellnet.lanparty.manager.configuration.PersistenceConfiguration
import org.thehellnet.lanparty.manager.configuration.SpringConfiguration
import org.thehellnet.lanparty.manager.configuration.WebSocketConfiguration
import org.thehellnet.lanparty.manager.model.constant.Role
import org.thehellnet.lanparty.manager.model.persistence.*
import org.thehellnet.lanparty.manager.repository.*
import spock.lang.Specification

@WebAppConfiguration
@ContextConfiguration(
        loader = AnnotationConfigWebContextLoader,
        classes = [
                SpringConfiguration,
                PersistenceConfiguration,
                JacksonConfiguration,
                WebSocketConfiguration
        ]
)
@Transactional
@Rollback
abstract class ContextSpecification extends Specification {

    protected static final String APPUSER_EMAIL = "email@email.com"
    protected static final String APPUSER_PASSWORD = "password"
    protected static final String APPUSER_NAME = "Name"
    protected static final String APPUSER_BARCODE = "0123456789"

    protected static final String APPUSER_PASSWORD_NEW = "password_new"
    protected static final String APPUSER_NAME_NEW = "Name2"
    protected static final String[] APPUSER_ROLES_NEW = [Role.LOGIN.name, Role.APPUSER_CHANGE_PASSWORD.name] as String[]
    protected static final String APPUSER_BARCODE_NEW = "9876543210"

    protected static final String APPUSERTOKEN = "0123456789abcdef"

    protected final static String TOURNAMENT_NAME = "Test Tournament"
    protected final static String TOURNAMENT_NAME_2 = "Test Tournament 2"
    protected final static String TOURNAMENT_CFG = "bind TAB \"+scores\"\n" +
            "bind ESCAPE \"togglemenu\"\n" +
            "bind SPACE \"+gostand\"\n" +
            "bind 4 \"+smoke\"\n" +
            "bind H \"say Google\""

    protected final static String GAME_TAG = "cod4"

    protected final static String SEAT_ADDRESS = "1.2.3.4"
    protected final static String SEAT_NAME = "Test seat"

    protected final static String TEAM_NAME = "Test team"

    protected final static String PLAYER_NICKNAME = "Testplayer"
    protected final static String PLAYER_BARCODE = "0123456789"

    protected final static String PLAYER_CFG = "bind H \"say Ciao\""

    protected final static String CFG = "unbindall\n" +
            "bind P \"quit\""

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

    @Transactional
    protected AppUser createAppUser() {
        AppUser appUser = appUserRepository.findByEmail(APPUSER_EMAIL)
        if (appUser == null) {
            appUser = new AppUser(APPUSER_EMAIL, APPUSER_PASSWORD, APPUSER_NAME, APPUSER_BARCODE)
            appUser = appUserRepository.save(appUser)
        }
        return appUser
    }

    @Transactional
    protected Tournament createTournament() {
        Tournament tournament = tournamentRepository.findByName(TOURNAMENT_NAME)
        if (tournament == null) {
            Game game = gameRepository.findByTag(GAME_TAG)
            tournament = new Tournament(name: TOURNAMENT_NAME, game: game, cfg: TOURNAMENT_CFG)
            tournament = tournamentRepository.save(tournament)
        }
        return tournament
    }

    @Transactional
    protected Seat createSeat() {
        Seat seat = seatRepository.findByIpAddress(SEAT_ADDRESS)
        if (seat == null) {
            Tournament tournament = createTournament()
            seat = new Seat(name: SEAT_NAME, ipAddress: SEAT_ADDRESS, tournament: tournament)
            seat = seatRepository.save(seat)
        }
        return seat
    }

    @Transactional
    protected Team createTeam() {
        Team team = teamRepository.findByName(TEAM_NAME)
        if (team == null) {
            Tournament tournament = createTournament()
            team = new Team(name: TEAM_NAME, tournament: tournament)
            team = teamRepository.save(team)
        }
        return team
    }

    @Transactional
    protected Player createPlayer() {
        Player player = playerRepository.findByNickname(PLAYER_NICKNAME)
        if (player == null) {
            AppUser appUser = createAppUser()
            Team team = createTeam()
            player = new Player(nickname: PLAYER_NICKNAME, appUser: appUser, team: team)
            player = playerRepository.save(player)
        }
        return player
    }

    @Transactional
    protected Cfg createCfg() {
        Player player = createPlayer()
        Game game = gameRepository.findByTag(GAME_TAG)
        Cfg cfg = cfgRepository.findByPlayerAndGame(player, game)
        if (cfg == null) {
            cfg = new Cfg(player: player, game: game, cfg: PLAYER_CFG)
            cfg = cfgRepository.save(cfg)
        }
        return cfg
    }
}
