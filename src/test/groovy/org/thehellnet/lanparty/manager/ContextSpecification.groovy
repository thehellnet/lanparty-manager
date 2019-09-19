package org.thehellnet.lanparty.manager

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.AnnotationConfigWebContextLoader
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.context.WebApplicationContext
import org.thehellnet.lanparty.manager.configuration.JacksonConfiguration
import org.thehellnet.lanparty.manager.configuration.PersistenceConfiguration
import org.thehellnet.lanparty.manager.configuration.SpringConfiguration
import org.thehellnet.lanparty.manager.configuration.WebSocketConfiguration
import org.thehellnet.lanparty.manager.model.persistence.*
import org.thehellnet.lanparty.manager.service.*
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
abstract class ContextSpecification extends Specification {

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

    @Autowired
    protected WebApplicationContext webApplicationContext

    @Autowired
    protected TournamentService tournamentService

    @Autowired
    protected SeatService seatService

    @Autowired
    protected GameService gameService

    @Autowired
    protected PlayerService playerService

    @Autowired
    protected TeamService teamService

    @Autowired
    protected CfgService cfgService

    protected Tournament createTournament() {
        Tournament tournament = tournamentService.findByName(TOURNAMENT_NAME)
        if (tournament == null) {
            Game game = gameService.findByTag(GAME_TAG)
            tournament = tournamentService.create(TOURNAMENT_NAME, game.id)
            tournament = tournamentService.save(tournament.id, null, null, null, TOURNAMENT_CFG)
        }
        return tournament
    }

    protected Seat createSeat() {
        Seat seat = seatService.findByAddress(SEAT_ADDRESS)
        if (seat == null) {
            Tournament tournament = createTournament()
            seat = seatService.create(SEAT_NAME, SEAT_ADDRESS, tournament.id)
        }
        return seat
    }

    protected Team createTeam() {
        Team team = teamService.findByName(TEAM_NAME)
        if (team == null) {
            Tournament tournament = createTournament()
            team = teamService.create(TEAM_NAME, tournament.id)
        }
        return team
    }

    protected Player createPlayer() {
        Player player = playerService.findByBarcode(PLAYER_BARCODE)
        if (player == null) {
            Team team = createTeam()
            player = playerService.create(PLAYER_NICKNAME, team.id)
            player = playerService.save(player.id, null, PLAYER_BARCODE, null)
        }
        return player
    }

    protected Cfg createCfg() {
        Player player = createPlayer()
        Game game = gameService.findByTag(GAME_TAG)
        Cfg cfg = cfgService.save(player.id, game.id, PLAYER_CFG)
        return cfg
    }

}
