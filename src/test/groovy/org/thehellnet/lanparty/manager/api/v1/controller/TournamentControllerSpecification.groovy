package org.thehellnet.lanparty.manager.api.v1.controller

class TournamentControllerSpecification extends ControllerSpecification {

//    @Autowired
//    private TournamentService tournamentService
//
//    private Tournament tournament
//
//    def setup() {
//        "Do login for token retrieving"()
//        tournament = createTournament()
//    }
//
//    def "getAll"() {
//        setup:
//        def requestBody = new JSONObject()
//
//        when:
//        def rawResponse = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/api/v1/public/tournament/getAll")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Auth-Token", token)
//                        .content(requestBody.toString())
//                )
//                .andReturn()
//                .response
//
//        then:
//        rawResponse.status == HttpStatus.OK.value()
//        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON
//
//        JSONObject response = new JSONObject(rawResponse.contentAsString)
//
//        response.has("success")
//        response.getBoolean("success")
//
//        response.has("data")
//        JSONArray tournaments = response.getJSONArray("data")
//
//        tournaments.length() == 1
//
//        JSONObject tournament = tournaments.getJSONObject(0)
//
//        tournament.has("id")
//        tournament.get("id") instanceof Integer
//
//        tournament.has("name")
//        tournament.get("name") == TOURNAMENT_NAME
//
//        tournament.has("gameId")
//        tournament.get("gameId") == gameService.findByTag(GAME_TAG).id
//    }
//
//    def "get"() {
//        given:
//        def requestBody = new JSONObject()
//        requestBody.put("id", tournament.getId())
//
//        when:
//        def rawResponse = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/api/v1/public/tournament/get")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Auth-Token", token)
//                        .content(requestBody.toString())
//                )
//                .andReturn()
//                .response
//
//        then:
//        rawResponse.status == HttpStatus.OK.value()
//        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON
//
//        JSONObject response = new JSONObject(rawResponse.contentAsString)
//        validateResponseAsJsonResponse(response)
//
//        response.getBoolean("success")
//        JSONObject tournamentObj = response.getJSONObject("data")
//
//        tournamentObj.has("id")
//        tournamentObj.getLong("id") == tournament.getId()
//
//        tournamentObj.has("name")
//        tournamentObj.get("name") == tournament.getName()
//
//        tournamentObj.has("game")
//        tournamentObj.get("game") == tournament.getGame().getId()
//
//        tournamentObj.has("status")
//        tournamentObj.get("status") == tournament.getStatus().name()
//    }
//
//    def "create"() {
//        given:
//        Game game = gameService.findByTag(GAME_TAG)
//
//        def requestBody = new JSONObject()
//        requestBody.put("name", TOURNAMENT_NAME_2)
//        requestBody.put("game", game.id)
//
//        when:
//        def rawResponse = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/api/v1/public/tournament/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Auth-Token", token)
//                        .content(requestBody.toString())
//                )
//                .andReturn()
//                .response
//
//        then:
//        rawResponse.status == HttpStatus.OK.value()
//        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON
//
//        JSONObject response = new JSONObject(rawResponse.contentAsString)
//        validateResponseAsJsonResponse(response)
//
//        response.getBoolean("success")
//        JSONObject tournament = response.getJSONObject("data")
//
//        tournament.has("name")
//        tournament.getString("name") == TOURNAMENT_NAME_2
//
//        tournament.has("game")
//        tournament.getLong("game") == game.getId()
//
//        tournament.has("status")
//        tournament.getString("status") == TournamentStatus.SCHEDULED.getName()
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
//        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON
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
//        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON
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
