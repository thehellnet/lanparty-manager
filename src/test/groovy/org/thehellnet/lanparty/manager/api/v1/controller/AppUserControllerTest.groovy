package org.thehellnet.lanparty.manager.api.v1.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

class AppUserControllerTest extends ControllerSpecification {

    def setup() {
        "Do login for token retrieving"();
    }

    def "isTokenValid without token"() {
        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/public/v1/appUser/isTokenValid")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.UNAUTHORIZED.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON
    }

    def "isTokenValid with empty token"() {
        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/public/v1/appUser/isTokenValid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Auth-Token", "")
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.UNAUTHORIZED.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON
    }

    def "isTokenValid with invalid token"() {
        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/public/v1/appUser/isTokenValid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Auth-Token", "0123456789abcdef")
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.UNAUTHORIZED.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON
    }

    def "isTokenValid with valid token"() {
        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/public/v1/appUser/isTokenValid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Auth-Token", token)
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.NO_CONTENT.value()
    }
}
