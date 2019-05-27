package org.thehellnet.lanparty.manager.api.v1.controller

import org.joda.time.DateTime
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.thehellnet.lanparty.manager.ContextTest

class GameControllerTest extends ContextTest {

    private String token

    def setup() {
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

    def "game list retrieving"() {
        given:
        def requestBody = new JSONObject([
                "token": token
        ])

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/game/list")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
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
        JSONArray data = response.getJSONArray("data")
        data.length() > 0

        data.toList().each { item ->
            JSONObject gameJson = (JSONObject) item
            gameJson.has("tag")
            ((String) gameJson.get("tag")).length() > 0
            gameJson.has("name")
            ((String) gameJson.get("name")).length() > 0
        }
    }
}
