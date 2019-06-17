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

    private static final String APPUSER_PASSWORD_NEW = "password_new"
    private static final String APPUSER_NAME_NEW = "Name2"
    private static final String[] APPUSER_ROLES_NEW = [Role.LOGIN.name, Role.APPUSER_CHANGE_PASSWORD.name] as String[]

    @Autowired
    private AppUserService appUserService

    def setup() {
        "Do login for token retrieving"()
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
        Long appUserId = 'retrieve test appUser ID and creates if not exists'()

        expect:
        appUserId != null

        when:
        JSONObject requestBody = new JSONObject()
        requestBody.put("id", appUserId)
        requestBody.put("name", APPUSER_NAME_NEW)
        requestBody.put("appUserRoles", new JSONArray(APPUSER_ROLES_NEW))

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

        appUserRoles.length() == APPUSER_ROLES_NEW.length

        "check number of appUsers in database"() == 2
    }

    def "delete"() {
        setup:
        Long appUserId = 'retrieve test appUser ID and creates if not exists'()

        expect:
        appUserId != null

        when:
        JSONObject requestBody = new JSONObject()
        requestBody.put("id", appUserId)

        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/appUser/delete")
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

        "check number of appUsers in database"() == 1
    }

    def "login"() {
        given:
        JSONObject requestBody = new JSONObject()
        requestBody.put("email", "admin")
        requestBody.put("password", "admin")

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

    def "changePassword"() {
        setup:
        Long appUserId = 'retrieve test appUser ID and creates if not exists'()
        String appUserToken = "get appUser token via login"()

        expect:
        appUserId != null
        appUserToken != null

        when: "Token of appUser \"admin\""
        JSONObject requestBody = new JSONObject()
        requestBody.put("oldPassword", APPUSER_PASSWORD)
        requestBody.put("newPassword", APPUSER_PASSWORD_NEW)

        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/appUser/changePassword")
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
        !response.getBoolean("success")

        when:
        "Token of appUser \"${APPUSER_NAME}\""
        requestBody = new JSONObject()
        requestBody.put("oldPassword", APPUSER_PASSWORD)
        requestBody.put("newPassword", APPUSER_PASSWORD_NEW)

        rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/appUser/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Auth-Token", appUserToken)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON_UTF8

        when:
        response = new JSONObject(rawResponse.contentAsString)

        then:
        response.has("success")
        response.getBoolean("success")
    }

    private int "check number of appUsers in database"() {
        return appUserService.getAll().size()
    }

    private Long 'retrieve test appUser ID and creates if not exists'() {
        AppUser appUser = appUserService.findByEmail(APPUSER_EMAIL)

        if (appUser != null) {
            appUserService.delete(appUser.id)
            appUser = null
        }

        if (appUser == null) {
            appUser = appUserService.create(APPUSER_EMAIL, APPUSER_PASSWORD, APPUSER_NAME)
        }

        appUser = appUserService.save(appUser.id, null, APPUSER_ROLES_NEW)

        return appUser.id
    }

    private String "get appUser token via login"() {
        JSONObject requestBody = new JSONObject()
        requestBody.put("email", APPUSER_EMAIL)
        requestBody.put("password", APPUSER_PASSWORD)

        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/appUser/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        rawResponse.status == HttpStatus.OK.value()
        JSONObject response = new JSONObject(rawResponse.contentAsString)

        if (!response.getBoolean("success")) {
            return null
        }

        JSONObject data = response.getJSONObject("data")

        return data.getString("token")
    }
}
