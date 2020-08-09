package org.thehellnet.lanparty.manager.api.v1.controller

import org.joda.time.DateTime
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.CustomMockMvcRequestBuilders
import org.thehellnet.lanparty.manager.model.persistence.*
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
            "name \"Testplayer\""

    private static final JSONObject REQUEST_EMPTY = new JSONObject()
    private static final String REQUEST_BODY_EMPTY = REQUEST_EMPTY.toString()

    private Tournament tournament
    private Seat seat
    private Team team
    private Player player
    private Cfg cfg

    def setup() {
        tournament = createTournament()
        seat = createSeat()
        team = createTeam()
        player = createPlayer()
        cfg = createCfg()
    }

    def "ping with not existing seat"() {
        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/public/v1/tool/ping", "5.6.7.8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY_EMPTY))
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
        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/public/v1/tool/ping", SEAT_ADDRESS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY_EMPTY))
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
        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/public/v1/tool/welcome", "5.6.7.8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY_EMPTY))
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
        DateTime lastContact = seat.lastContact

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/public/v1/tool/welcome", SEAT_ADDRESS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY_EMPTY))
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        when:
        JSONObject response = new JSONObject(rawResponse.contentAsString)
        seat = seatRepository.findById(seat.id).orElseThrow()

        then:
        response.isEmpty()
        seat.lastContact.isAfter(lastContact)
    }

    def "getCfg with not existing seat and not existing barcode"() {
        given:
        JSONObject requestBody = new JSONObject()
        requestBody.put("barcode", "not-existing")

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/public/v1/tool/getCfg", "5.6.7.8")
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

    def "getCfg with not existing seat and existing barcode"() {
        given:
        JSONObject requestBody = new JSONObject()
        requestBody.put("barcode", APPUSER_BARCODE)

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/public/v1/tool/getCfg", "5.6.7.8")
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

    def "getCfg with existing seat and not existing barcode"() {
        given:
        JSONObject requestBody = new JSONObject()
        requestBody.put("barcode", "not.existing")

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/public/v1/tool/getCfg", SEAT_ADDRESS)
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

    def "getCfg with existing seat and existing barcode"() {
        given:
        AppUser appUser = appUserRepository.findByBarcode(APPUSER_BARCODE)
        Tournament tournament = createTournament()
        Player player = playerRepository.findByAppUserAndTournament(appUser, tournament)
        DateTime lastContact = seat.lastContact

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
        "Player is in one seat only"(player)

        when:
        JSONArray response = new JSONArray(rawResponse.contentAsString)
        String cfg = StringUtility.joinLines(response.toList() as List<String>)
        seat = seatRepository.findById(seat.id).orElseThrow()

        then:
        cfg == PLAYER_CFG_SANITIZED
        seat.lastContact.isAfter(lastContact)
    }

    def "saveCfg with not existing seat and not existing barcode"() {
        given:
        JSONObject requestBody = new JSONObject()
        requestBody.put("barcode", "not.existing")
        requestBody.put("cfgLines", StringUtility.splitLines(PLAYER_CFG_SANITIZED))

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/public/v1/tool/saveCfg", "5.6.7.8")
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

    def "saveCfg with not existing seat and existing barcode"() {
        given:
        JSONObject requestBody = new JSONObject()
        requestBody.put("barcode", APPUSER_BARCODE)
        requestBody.put("cfgLines", StringUtility.splitLines(PLAYER_CFG_SANITIZED))

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/public/v1/tool/saveCfg", "5.6.7.8")
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

    def "saveCfg with existing seat and not existing barcode"() {
        given:
        JSONObject requestBody = new JSONObject()
        requestBody.put("barcode", "not.existing")
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
        rawResponse.status == HttpStatus.NOT_FOUND.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        when:
        JSONObject response = new JSONObject(rawResponse.contentAsString)

        then:
        response.isEmpty() || response.has("message")
    }

    def "saveCfg with existing seat and existing barcode"() {
        given:
        JSONObject requestBody = new JSONObject()
        requestBody.put("barcode", APPUSER_BARCODE)
        requestBody.put("cfgLines", StringUtility.splitLines(PLAYER_CFG_SANITIZED))
        String currentCfgContent = cfg.cfgContent

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
        cfg = cfgRepository.findById(cfg.id).orElseThrow()

        then:
        response.isEmpty()
        cfg.cfgContent != currentCfgContent
        cfg.cfgContent == PLAYER_CFG_SANITIZED
    }
}
