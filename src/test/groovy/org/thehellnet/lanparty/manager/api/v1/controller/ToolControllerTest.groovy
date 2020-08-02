package org.thehellnet.lanparty.manager.api.v1.controller

import org.json.JSONArray
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.CustomMockMvcRequestBuilders
import org.thehellnet.lanparty.manager.model.persistence.AppUser
import org.thehellnet.lanparty.manager.model.persistence.Player
import org.thehellnet.lanparty.manager.model.persistence.Tournament
import org.thehellnet.utility.StringUtility

class ToolControllerTest extends ControllerSpecification {

    private static final String PLAYER_CFG_SANITIZED = "unbindall\n" +
            "bind TAB \"+scores\"\n" +
            "bind ESCAPE \"togglemenu\"\n" +
            "bind SPACE \"+gostand\"\n" +
            "bind 4 \"+smoke\"\n" +
            "bind H \"say Ciao\"\n" +
            "bind . \"exec lanpartytool\"\n" +
            "bind , \"writeconfig lanpartydump\"\n" +
            "name Testplayer"

    def setup() {
        createTournament()
        createSeat()
        createTeam()
        createPlayer()
        createCfg()
    }

    def "ping with not existing seat"() {
        given:
        JSONObject requestBody = new JSONObject()

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/public/v1/tool/ping", "5.6.7.8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        when:
        JSONObject response = new JSONObject(rawResponse.contentAsString)

        then:
        response.isEmpty()
    }

    def "ping with existing seat"() {
        given:
        JSONObject requestBody = new JSONObject()

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/public/v1/tool/ping", SEAT_ADDRESS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        when:
        JSONObject response = new JSONObject(rawResponse.contentAsString)

        then:
        response.isEmpty()
    }

    def "welcome with not existing seat"() {
        given:
        JSONObject requestBody = new JSONObject()

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/public/v1/tool/welcome", "5.6.7.8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.NOT_FOUND.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        when:
        JSONObject response = new JSONObject(rawResponse.contentAsString)

        then:
        response.isEmpty() || response.has("message")
    }

    def "welcome with existing seat"() {
        given:
        JSONObject requestBody = new JSONObject()

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/public/v1/tool/welcome", SEAT_ADDRESS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        when:
        JSONObject response = new JSONObject(rawResponse.contentAsString)

        then:
        response.isEmpty()
    }

    def "getCfg with existing seat and existing barcode"() {
        given:
        AppUser appUser = appUserRepository.findByBarcode(PLAYER_BARCODE)
        Tournament tournament = createTournament()
        Player player = playerRepository.findByAppUserAndTournament(appUser, tournament)

        JSONObject requestBody = new JSONObject()
        requestBody.put("barcode", appUser.barcode)

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/public/v1/tool/getCfg", SEAT_ADDRESS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON
        "Player is in one seat only"(player);

        when:
        JSONArray response = new JSONArray(rawResponse.contentAsString)
        String cfg = StringUtility.joinLines(response.toList() as List<String>)

        then:
        cfg == PLAYER_CFG_SANITIZED
    }

    def "saveCfg with existing seat and existing barcode"() {
        given:
        JSONObject requestBody = new JSONObject()
        requestBody.put("barcode", PLAYER_BARCODE)
        requestBody.put("cfgLines", StringUtility.splitLines(PLAYER_CFG_SANITIZED))

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/public/v1/tool/saveCfg", SEAT_ADDRESS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        when:
        JSONObject response = new JSONObject(rawResponse.contentAsString)

        then:
        response.isEmpty()
    }
}
