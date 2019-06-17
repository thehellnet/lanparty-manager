package org.thehellnet.lanparty.manager.api.v1.controller

import org.joda.time.DateTime
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.thehellnet.lanparty.manager.model.constant.Role
import org.thehellnet.lanparty.manager.model.persistence.AppUser
import org.thehellnet.lanparty.manager.service.AppUserService
import org.thehellnet.utility.TokenUtility

class AppUserControllerSpecification extends ControllerSpecification {

    private static final String APPUSER_EMAIL = "email@email.com"
    private static final String APPUSER_PASSWORD = "password"
    private static final String APPUSER_NAME = "Name"

    private static final String APPUSER_NAME_NEW = "Name2"
    private static final String APPUSER_ROLES_NEW = Role.LOGIN.name

    @Autowired
    private AppUserService appUserService

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

        "check number of appUsers in database"() == 2
    }

    def "save"() {
        setup:
        Long appUserId = "Retrieve test appUser ID and creates if not exists"()

        expect:
        appUserId != null

        when:
        JSONObject requestBody = new JSONObject()
        requestBody.put("id", appUserId)
        requestBody.put("name", APPUSER_NAME_NEW)
        requestBody.put("appUserRoles", new JSONArray([APPUSER_ROLES_NEW]))

        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/appUser/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Auth-Token", token)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON_UTF8

        when:
        JSONObject response = new JSONObject(rawResponse.contentAsString)

        then:
        response.has("success")
        response.getBoolean("success")

        response.has("data")

        when:
        JSONObject data = response.getJSONObject("data")

        then:
        data.has("appUser")
        JSONObject appUser = data.getJSONObject("appUser")

        !appUser.has("password")

        appUser.has("id")
        appUser.get("id") instanceof Integer
        appUser.getInt("id") == appUserId

        appUser.has("email")
        appUser.get("email") instanceof String
        appUser.getString("email") == APPUSER_EMAIL

        appUser.has("name")
        appUser.get("name") instanceof String
        appUser.getString("name") == APPUSER_NAME_NEW

        appUser.has("appUserRoles")
        JSONArray appUserRoles = appUser.getJSONArray("appUserRoles")

        appUserRoles.length() == 1
        appUserRoles.getString(0) == APPUSER_ROLES_NEW

        "check number of appUsers in database"() == 2
    }

    def "delete"() {
        setup:
        Long appUserId = "Retrieve test appUser ID and creates if not exists"()

        expect:
        appUserId != null

        when:
        JSONObject requestBody = new JSONObject()
        requestBody.put("id", appUserId)
        requestBody.put("name", APPUSER_NAME_NEW)
        requestBody.put("appUserRoles", new JSONArray([APPUSER_ROLES_NEW]))

        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/appUser/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Auth-Token", token)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON_UTF8

        when:
        JSONObject response = new JSONObject(rawResponse.contentAsString)

        then:
        response.has("success")
        response.getBoolean("success")

        response.has("data")

        when:
        JSONObject data = response.getJSONObject("data")

        then:
        data.has("appUser")
        JSONObject appUser = data.getJSONObject("appUser")

        !appUser.has("password")

        appUser.has("id")
        appUser.get("id") instanceof Integer
        appUser.getLong("id") == appUserId

        appUser.has("email")
        appUser.get("email") instanceof String
        appUser.getString("email") == APPUSER_EMAIL

        appUser.has("name")
        appUser.get("name") instanceof String
        appUser.getString("name") == APPUSER_NAME_NEW

        appUser.has("appUserRoles")
        JSONArray appUserRoles = appUser.getJSONArray("appUserRoles")

        appUserRoles.length() == 1
        appUserRoles.getString(0) == APPUSER_ROLES_NEW

        "check number of appUsers in database"() == 2
    }

    private int "check number of appUsers in database"() {
        return appUserService.getAll().size()
    }

    private Long "Retrieve test appUser ID and creates if not exists"() {
        AppUser appUser = appUserService.findByEmail(APPUSER_EMAIL)

        if (appUser == null) {
            appUser = appUserService.create(APPUSER_EMAIL, APPUSER_PASSWORD, APPUSER_NAME)
        }

        return appUser.id
    }
}
