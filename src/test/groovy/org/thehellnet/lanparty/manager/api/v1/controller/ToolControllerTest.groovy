package org.thehellnet.lanparty.manager.api.v1.controller

import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.CustomMockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

class ToolControllerTest extends ControllerSpecification {

    def setup() {
        createTournament()
    }

    def "Ping with not existing seat"() {
        given:
        JSONObject requestBody = new JSONObject()

        when:
        def rawResponse = mockMvc
                .perform(CustomMockMvcRequestBuilders
                        .postCustomRemoteAddress("/api/v1/tool/ping", "1.2.3.4")
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
        response.has("success")
        !response.getBoolean("success")

        response.has("data")
        response.get("data") == JSONObject.NULL

        response.has("errors")

        when:
        JSONObject errors = response.getJSONObject("errors")

        then:
        errors.getString("message").length() > 0
    }

    def "Ping with existing seat"() {
        given:
        JSONObject requestBody = new JSONObject()

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/tool/ping")
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
        response.has("success")
        !response.getBoolean("success")

        response.has("data")
        response.get("data") == JSONObject.NULL

        response.has("errors")

        when:
        JSONObject errors = response.getJSONObject("errors")

        then:
        errors.getString("message").length() > 0
    }
}
