package org.thehellnet.lanparty.manager.api.v1.controller

import org.joda.time.DateTime
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.thehellnet.lanparty.manager.ContextSpecification
import org.thehellnet.lanparty.manager.model.persistence.*
import org.thehellnet.lanparty.manager.service.*

abstract class ControllerSpecification extends ContextSpecification {

    protected final static String TOURNAMENT_NAME = "Test Tournament"
    protected final static String TOURNAMENT_NAME_2 = "Test Tournament 2"
    protected final static String TOURNAMENT_CFG = "unbindall\n" +
            "bind TAB \"+scores\"\n" +
            "bind ESCAPE \"togglemenu\"\n" +
            "bind SPACE \"+gostand\"\n" +
            "bind 4 \"+smoke\"\n" +
            "bind H \"say Google\"\n" +
            "bind . \"exec lanpartytool\"\n" +
            "bind , \"writeconfig lanpartydump\""

    protected final static String GAME_TAG = "cod4"

    protected final static String SEAT_ADDRESS = "1.2.3.4"
    protected final static String SEAT_NAME = "Test seat"

    protected final static String TEAM_NAME = "Test team"

    protected final static String PLAYER_NICKNAME = "Test player"
    protected final static String PLAYER_BARCODE = "0123456789"

    protected final static String PLAYER_CFG = "bind H \"say Ciao\""

    protected final static String PLAYER_CFG_SANITIZED = "unbindall\n" +
            "bind TAB \"+scores\"\n" +
            "bind ESCAPE \"togglemenu\"\n" +
            "bind SPACE \"+gostand\"\n" +
            "bind 4 \"+smoke\"\n" +
            "bind H \"say Ciao\"\n" +
            "bind . \"exec lanpartytool\"\n" +
            "bind , \"writeconfig lanpartydump\""

    protected String token

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

    protected void "Do login for token retrieving"() {
        if (token != null) {
            return
        }

        def requestBody = new JSONObject([
                "email"   : "admin",
                "password": "admin"
        ])

        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/appUser/login")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON_UTF8

        JSONObject response = new JSONObject(rawResponse.contentAsString)
        response.has("success")
        response.getBoolean("success")

        response.has("data")
        JSONObject data = response.getJSONObject("data")

        data.has("expiration")
        new DateTime(data.getLong("expiration")).isAfterNow()

        data.has("token")
        token = data.getString("token")
    }

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

    protected static void validateResponseAsJsonResponse(JSONObject response) {
        assert response.has("success")
        assert response.has("data")
        assert response.has("errors")
    }
}
