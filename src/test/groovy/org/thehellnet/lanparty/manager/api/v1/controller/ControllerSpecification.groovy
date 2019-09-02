package org.thehellnet.lanparty.manager.api.v1.controller

import org.joda.time.DateTime
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.thehellnet.lanparty.manager.ContextSpecification
import org.thehellnet.lanparty.manager.model.persistence.Game
import org.thehellnet.lanparty.manager.model.persistence.Tournament
import org.thehellnet.lanparty.manager.service.GameService
import org.thehellnet.lanparty.manager.service.TournamentService

abstract class ControllerSpecification extends ContextSpecification {

    protected final static String TOURNAMENT_NAME = "Test Tournament"
    protected final static String TOURNAMENT_NAME_2 = "Test Tournament 2"

    protected final static String GAME_TAG = "cod4"

    protected String token

    @Autowired
    protected TournamentService tournamentService

    @Autowired
    protected GameService gameService

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
        }
        return tournament
    }
}
