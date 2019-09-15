package org.thehellnet.lanparty.manager.api.v1.controller

import org.json.JSONArray
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.CustomMockMvcRequestBuilders
import org.thehellnet.utility.StringUtility

class ToolControllerTest extends ControllerSpecification {

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
                        .post("/api/v1/tool/ping", "5.6.7.8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON_UTF8

        when:
        JSONObject response = new JSONObject(rawResponse.contentAsString)

        then:
        validateResponseAsJsonResponse(response)

        response.getBoolean("success")
        response.get("data") == JSONObject.NULL
    }

    def "ping with existing seat"() {
        given:
        JSONObject requestBody = new JSONObject()

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/v1/tool/ping", SEAT_ADDRESS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON_UTF8

        when:
        JSONObject response = new JSONObject(rawResponse.contentAsString)

        then:
        validateResponseAsJsonResponse(response)

        response.getBoolean("success")
        response.get("data") == JSONObject.NULL
    }

    def "welcome with not existing seat"() {
        given:
        JSONObject requestBody = new JSONObject()

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/v1/tool/welcome", "5.6.7.8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON_UTF8

        when:
        JSONObject response = new JSONObject(rawResponse.contentAsString)

        then:
        validateResponseAsJsonResponse(response)

        !response.getBoolean("success")
        response.get("data") == JSONObject.NULL

        response.has("errors")

        when:
        JSONObject errors = response.getJSONObject("errors")

        then:
        errors.getString("message").length() > 0
    }

    def "welcome with existing seat"() {
        given:
        JSONObject requestBody = new JSONObject()

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/v1/tool/welcome", SEAT_ADDRESS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON_UTF8

        when:
        JSONObject response = new JSONObject(rawResponse.contentAsString)

        then:
        validateResponseAsJsonResponse(response)

        response.getBoolean("success")

        when:
        JSONObject data = response.getJSONObject("data")

        then:
        data.has("name")
        data.getString("name") == SEAT_NAME
    }

    def "getCfg with existing seat and existing barcode"() {
        given:
        JSONObject requestBody = new JSONObject()
        requestBody.put("barcode", PLAYER_BARCODE)

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .post("/api/v1/tool/getCfg", SEAT_ADDRESS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON_UTF8

        when:
        JSONObject response = new JSONObject(rawResponse.contentAsString)

        then:
        validateResponseAsJsonResponse(response)

        response.getBoolean("success")

        when:
        JSONArray data = response.getJSONArray("data")
        String cfg = StringUtility.joinLines(data.toList() as List<String>);

        then:
        cfg == PLAYER_CFG_SANITIZED
    }
}
