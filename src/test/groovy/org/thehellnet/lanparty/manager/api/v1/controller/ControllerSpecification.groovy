package org.thehellnet.lanparty.manager.api.v1.controller

import org.joda.time.DateTime
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.thehellnet.lanparty.manager.ContextSpecification

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
                        .post("/api/v1/public/appUser/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        JSONObject response = new JSONObject(rawResponse.contentAsString)

        response.has("expiration")
        new DateTime(response.getLong("expiration")).isAfterNow()

        response.has("token")
        token = response.getString("token")
    }

    protected static void validateResponseAsJsonResponse(JSONObject response) {
        assert response.has("success")
        assert response.has("data")
        assert response.has("errors")
    }
}
