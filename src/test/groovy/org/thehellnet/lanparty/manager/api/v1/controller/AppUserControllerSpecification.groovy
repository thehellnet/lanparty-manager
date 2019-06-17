package org.thehellnet.lanparty.manager.api.v1.controller

import org.joda.time.DateTime
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.thehellnet.lanparty.manager.model.constant.Role
import org.thehellnet.utility.TokenUtility

class AppUserControllerSpecification extends ControllerSpecification {

    private static final String APPUSER_EMAIL = "email@email.com"
    private static final String APPUSER_PASSWORD = "password"
    private static final String APPUSER_NAME = "Name"

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
        data.getString("token").length() == TokenUtility.LENGTH
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

        JSONObject appUser = appUsers.getJSONObject(0)

        appUser.has("id")
        appUser.get("id") instanceof Integer

        appUser.has("email")
        appUser.get("email") instanceof String

        appUser.has("name")
        appUser.get("name") == JSONObject.NULL
    }

    def "get"() {
        given:
        def requestBody = new JSONObject()
        requestBody.put("id", 1)

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/appUser/get")
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

        data.has("appUser")
        JSONObject appUser = data.getJSONObject("appUser")

        !appUser.has("password")

        appUser.has("id")
        appUser.get("id") instanceof Integer

        appUser.has("email")
        appUser.get("email") instanceof String

        appUser.has("name")
        appUser.get("name") == JSONObject.NULL

        appUser.has("appUserRoles")
        JSONArray appUserRoles = appUser.getJSONArray("appUserRoles")

        appUserRoles.length() == Role.values().length

        for (int i = 0; i < appUserRoles.length(); i++) {
            Role role = Role.valueOf(appUserRoles.getString(i))
            assert Role.values().contains(role)
        }
    }

    def "create"() {
        given:
        def requestBody = new JSONObject()
        requestBody.put("email", APPUSER_EMAIL)
        requestBody.put("password", APPUSER_PASSWORD)
        requestBody.put("name", APPUSER_NAME)

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/appUser/create")
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

        data.has("appUser")
        JSONObject appUser = data.getJSONObject("appUser")

        !appUser.has("password")

        appUser.has("id")
        appUser.get("id") instanceof Integer

        appUser.has("email")
        appUser.get("email") instanceof String
        appUser.getString("email") == APPUSER_EMAIL

        appUser.has("name")
        appUser.get("name") instanceof String
        appUser.getString("name") == APPUSER_NAME

        appUser.has("appUserRoles")
        JSONArray appUserRoles = appUser.getJSONArray("appUserRoles")

        appUserRoles.length() == 0

        checkAppUserNumber() == 2
    }

    private int checkAppUserNumber() {
        JSONObject requestBody = new JSONObject()

        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/appUser/getAll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Auth-Token", token)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON_UTF8

        JSONObject response = new JSONObject(rawResponse.contentAsString)

        response.has("success")
        assert response.getBoolean("success")

        JSONObject data = response.getJSONObject("data")
        JSONArray appUsers = data.getJSONArray("appUsers")
        return appUsers.length()
    }

}
