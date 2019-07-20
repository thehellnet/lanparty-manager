package org.thehellnet.lanparty.manager.api.v1.controller


import org.json.JSONArray
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.thehellnet.lanparty.manager.model.constant.Role
import org.thehellnet.lanparty.manager.model.persistence.Tournament
import org.thehellnet.lanparty.manager.service.TournamentService

class TournamentControllerSpecification extends ControllerSpecification {

    private static final String APPUSER_EMAIL = "email@email.com"
    private static final String APPUSER_PASSWORD = "password"
    private static final String APPUSER_NAME = "Name"

    private static final String APPUSER_PASSWORD_NEW = "password_new"
    private static final String APPUSER_NAME_NEW = "Name2"
    private static final String[] APPUSER_ROLES_NEW = [Role.LOGIN.name, Role.APPUSER_CHANGE_PASSWORD.name] as String[]

    @Autowired
    private TournamentService tournamentService

    private Tournament tournament

    def setup() {
        "Do login for token retrieving"()
        tournament = createTournament()
    }

    def "getAll"() {
        setup:
        def requestBody = new JSONObject()

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/tournament/getAll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Auth-Token", token)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON_UTF8

        JSONObject response = new JSONObject(rawResponse.contentAsString)

        response.has("success")
        response.getBoolean("success")

        response.has("data")
        JSONArray tournaments = response.getJSONArray("data")

        tournaments.length() == 1

        JSONObject tournament = tournaments.getJSONObject(0)

        tournament.has("id")
        tournament.get("id") instanceof Integer

        tournament.has("name")
        tournament.get("name") == TOURNAMENT_NAME

        tournament.has("gameId")
        tournament.get("gameId") == gameService.findByTag(GAME_TAG).id
    }

    def "get"() {
        given:
        def requestBody = new JSONObject()
        requestBody.put("id", tournament.getId())

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/tournament/get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Auth-Token", token)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON_UTF8

        JSONObject response = new JSONObject(rawResponse.contentAsString)

        response.has("success")
        response.getBoolean("success")

        response.has("data")
        JSONObject tournamentObj = response.getJSONObject("data")

        tournamentObj.has("id")
        tournamentObj.getLong("id") == tournament.getId()

        tournamentObj.has("name")
        tournamentObj.get("name") == tournament.getName()

        tournamentObj.has("game")
        tournamentObj.get("game") == tournament.getGame().getId()

        tournamentObj.has("status")
        tournamentObj.get("status") == tournament.getStatus().name()
    }

//    def "create"() {
//        given:
//        def requestBody = new JSONObject()
//        requestBody.put("email", APPUSER_EMAIL)
//        requestBody.put("password", APPUSER_PASSWORD)
//        requestBody.put("name", APPUSER_NAME)
//
//        when:
//        def rawResponse = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/api/v1/public/appUser/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Auth-Token", token)
//                        .content(requestBody.toString())
//                )
//                .andReturn()
//                .response
//
//        then:
//        rawResponse.status == HttpStatus.OK.value()
//        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON_UTF8
//
//        JSONObject response = new JSONObject(rawResponse.contentAsString)
//
//        response.has("success")
//        response.getBoolean("success")
//
//        response.has("data")
//        JSONObject appUser = response.getJSONObject("data")
//
//        !appUser.has("password")
//
//        appUser.has("id")
//        appUser.get("id") instanceof Integer
//
//        appUser.has("email")
//        appUser.get("email") instanceof String
//        appUser.getString("email") == APPUSER_EMAIL
//
//        appUser.has("name")
//        appUser.get("name") instanceof String
//        appUser.getString("name") == APPUSER_NAME
//
//        appUser.has("appUserRoles")
//        JSONArray appUserRoles = appUser.getJSONArray("appUserRoles")
//
//        appUserRoles.length() == 0
//
//        "check number of appUsers in database"() == 2
//    }
//
//    def "save"() {
//        setup:
//        Long appUserId = 'retrieve test appUser ID and creates if not exists'()
//
//        expect:
//        appUserId != null
//
//        when:
//        JSONObject requestBody = new JSONObject()
//        requestBody.put("id", appUserId)
//        requestBody.put("name", APPUSER_NAME_NEW)
//        requestBody.put("appUserRoles", new JSONArray(APPUSER_ROLES_NEW))
//
//        def rawResponse = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/api/v1/public/appUser/save")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Auth-Token", token)
//                        .content(requestBody.toString())
//                )
//                .andReturn()
//                .response
//
//        then:
//        rawResponse.status == HttpStatus.OK.value()
//        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON_UTF8
//
//        when:
//        JSONObject response = new JSONObject(rawResponse.contentAsString)
//
//        then:
//        response.has("success")
//        response.getBoolean("success")
//
//        response.has("data")
//
//        when:
//        JSONObject appUser = response.getJSONObject("data")
//
//        then:
//        !appUser.has("password")
//
//        appUser.has("id")
//        appUser.get("id") instanceof Integer
//        appUser.getLong("id") == appUserId
//
//        appUser.has("email")
//        appUser.get("email") instanceof String
//        appUser.getString("email") == APPUSER_EMAIL
//
//        appUser.has("name")
//        appUser.get("name") instanceof String
//        appUser.getString("name") == APPUSER_NAME_NEW
//
//        appUser.has("appUserRoles")
//        JSONArray appUserRoles = appUser.getJSONArray("appUserRoles")
//
//        appUserRoles.length() == APPUSER_ROLES_NEW.length
//
//        "check number of appUsers in database"() == 2
//    }
//
//    def "delete"() {
//        setup:
//        Long appUserId = 'retrieve test appUser ID and creates if not exists'()
//
//        expect:
//        appUserId != null
//
//        when:
//        JSONObject requestBody = new JSONObject()
//        requestBody.put("id", appUserId)
//
//        def rawResponse = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/api/v1/public/appUser/delete")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Auth-Token", token)
//                        .content(requestBody.toString())
//                )
//                .andReturn()
//                .response
//
//        then:
//        rawResponse.status == HttpStatus.OK.value()
//        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON_UTF8
//
//        when:
//        JSONObject response = new JSONObject(rawResponse.contentAsString)
//
//        then:
//        response.has("success")
//        response.getBoolean("success")
//
//        "check number of appUsers in database"() == 1
//    }
}
