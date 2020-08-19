package org.thehellnet.lanparty.manager.api.v1.controller

import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.thehellnet.lanparty.manager.ContextSpecification

import java.time.LocalDateTime

abstract class ControllerSpecification extends ContextSpecification {

    protected String token

    protected MockMvc mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

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
                        .post("/api/public/v1/appUser/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        JSONObject response = new JSONObject(rawResponse.contentAsString)

        response.has("expiration")
        // TODO: Fix check
//        LocalDateTime.ofEpochSecond(response.getLong("expiration") / 1000, response.getLong("expiration") % 1000).isAfter(LocalDateTime.now())

        response.has("token")
        token = response.getString("token")
    }

    protected static boolean validateResponseAsJsonResponse(JSONObject response) {
        return response.has("success")
                && response.has("data")
                && response.has("errors")
    }
}
