package org.thehellnet.lanparty.manager.api.v1.controller

import org.joda.time.DateTime
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

class AppUserControllerSpecification extends ControllerSpecification {

    def setup() {
        "Do login for token retrieving"()
    }

    def "login"() {
        given:
        def requestBody = new JSONObject([
                "email"   : "admin",
                "password": "admin"
        ])

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/appUser/login")
                        .contentType(MediaType.APPLICATION_JSON)
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

        JSONObject data = response.getJSONObject("data")

        data.has("expiration")
        new DateTime(data.getLong("expiration")).isAfterNow()

        data.has("token")
        data.getString("token").length() > 0
    }

    def "getAll"() {
        given:
        def requestBody = new JSONObject()

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/appUser/getAll")
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
        JSONObject data = response.getJSONObject("data")

        data.has("appUsers")
        JSONArray appUsers = data.getJSONArray("appUsers")

        appUsers.length() == 1
    }
}
