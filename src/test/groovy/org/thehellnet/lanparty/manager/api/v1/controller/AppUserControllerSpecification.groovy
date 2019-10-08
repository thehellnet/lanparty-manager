package org.thehellnet.lanparty.manager.api.v1.controller


import org.json.JSONArray
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.thehellnet.lanparty.manager.model.constant.Role
import org.thehellnet.lanparty.manager.service.AppUserService
import spock.lang.Unroll

class AppUserControllerSpecification extends ControllerSpecification {

    private static final String APPUSER_EMAIL = "email@email.com"
    private static final String APPUSER_PASSWORD = "password"
    private static final String APPUSER_NAME = "Name"
    private static final String APPUSER_BARCODE = "0123456789"

    private static final String APPUSER_PASSWORD_NEW = "password_new"
    private static final String APPUSER_NAME_NEW = "Name2"
    private static final String[] APPUSER_ROLES_NEW = [Role.LOGIN.name, Role.APPUSER_CHANGE_PASSWORD.name] as String[]
    private static final String APPUSER_BARCODE_NEW = "9876543210"

    @Autowired
    private AppUserService appUserService

    def setup() {
        "Do login for token retrieving"()
    }

    @Unroll
    def "create success with '#email' email, '#password' password, '#name' name and '#barcode' barcode"(String email, String password, String name, String barcode) {
        given:
        def requestBody = new JSONObject()
        requestBody.put("email", email)
        requestBody.put("password", password)
        requestBody.put("name", name)
        requestBody.put("barcode", barcode)

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/appUser")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.CREATED.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        when:
        JSONObject appUser = new JSONObject(rawResponse.contentAsString)

        then:
        !appUser.has("password")

        appUser.has("id")
        appUser.get("id") instanceof Integer

        appUser.has("email")
        appUser.get("email") instanceof String
        appUser.getString("email") == email

        appUser.has("name")
        if (name != null) {
            appUser.get("name") instanceof String
            appUser.getString("name") == name
        } else {
            appUser.isNull("name")
        }

        appUser.has("barcode")
        if (barcode != null) {
            appUser.get("barcode") instanceof String
            appUser.getString("barcode") == barcode
        } else {
            appUser.isNull("barcode")
        }

        appUser.has("appUserRoles")

        when:
        JSONArray appUserRoles = appUser.getJSONArray("appUserRoles")

        then:
        appUserRoles.length() == 0

        and:
        "check number of appUsers in database"() == 2

        where:
        email         | password         | name         | barcode
        APPUSER_EMAIL | APPUSER_PASSWORD | null         | null
        APPUSER_EMAIL | APPUSER_PASSWORD | null         | ""
        APPUSER_EMAIL | APPUSER_PASSWORD | null         | APPUSER_BARCODE
        APPUSER_EMAIL | APPUSER_PASSWORD | ""           | null
        APPUSER_EMAIL | APPUSER_PASSWORD | ""           | ""
        APPUSER_EMAIL | APPUSER_PASSWORD | ""           | APPUSER_BARCODE
        APPUSER_EMAIL | APPUSER_PASSWORD | APPUSER_NAME | null
        APPUSER_EMAIL | APPUSER_PASSWORD | APPUSER_NAME | ""
        APPUSER_EMAIL | APPUSER_PASSWORD | APPUSER_NAME | APPUSER_BARCODE

    }

    @Unroll
    def "create error with '#email' email, '#password' password, '#name' name and '#barcode' barcode"(String email, String password, String name, String barcode) {
        given:
        def requestBody = new JSONObject()
        requestBody.put("email", email)
        requestBody.put("password", password)
        requestBody.put("name", name)

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/public/appUser")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.UNPROCESSABLE_ENTITY.value()

        and:
        "check number of appUsers in database"() == 1

        where:
        email         | password         | name         | barcode
        null          | null             | null         | null
        null          | null             | null         | ""
        null          | null             | null         | APPUSER_BARCODE
        null          | null             | ""           | null
        null          | null             | ""           | ""
        null          | null             | ""           | APPUSER_BARCODE
        null          | null             | APPUSER_NAME | null
        null          | null             | APPUSER_NAME | ""
        null          | null             | APPUSER_NAME | APPUSER_BARCODE
        null          | ""               | null         | null
        null          | ""               | null         | ""
        null          | ""               | null         | APPUSER_BARCODE
        null          | ""               | ""           | null
        null          | ""               | ""           | ""
        null          | ""               | ""           | APPUSER_BARCODE
        null          | ""               | APPUSER_NAME | null
        null          | ""               | APPUSER_NAME | ""
        null          | ""               | APPUSER_NAME | APPUSER_BARCODE
        null          | APPUSER_PASSWORD | null         | null
        null          | APPUSER_PASSWORD | null         | ""
        null          | APPUSER_PASSWORD | null         | APPUSER_BARCODE
        null          | APPUSER_PASSWORD | ""           | null
        null          | APPUSER_PASSWORD | ""           | ""
        null          | APPUSER_PASSWORD | ""           | APPUSER_BARCODE
        null          | APPUSER_PASSWORD | APPUSER_NAME | null
        null          | APPUSER_PASSWORD | APPUSER_NAME | ""
        null          | APPUSER_PASSWORD | APPUSER_NAME | APPUSER_BARCODE
        ""            | null             | null         | null
        ""            | null             | null         | ""
        ""            | null             | null         | APPUSER_BARCODE
        ""            | null             | ""           | null
        ""            | null             | ""           | ""
        ""            | null             | ""           | APPUSER_BARCODE
        ""            | null             | APPUSER_NAME | null
        ""            | null             | APPUSER_NAME | ""
        ""            | null             | APPUSER_NAME | APPUSER_BARCODE
        ""            | ""               | null         | null
        ""            | ""               | null         | ""
        ""            | ""               | null         | APPUSER_BARCODE
        ""            | ""               | ""           | null
        ""            | ""               | ""           | ""
        ""            | ""               | ""           | APPUSER_BARCODE
        ""            | ""               | APPUSER_NAME | null
        ""            | ""               | APPUSER_NAME | ""
        ""            | ""               | APPUSER_NAME | APPUSER_BARCODE
        ""            | APPUSER_PASSWORD | null         | null
        ""            | APPUSER_PASSWORD | null         | ""
        ""            | APPUSER_PASSWORD | null         | APPUSER_BARCODE
        ""            | APPUSER_PASSWORD | ""           | null
        ""            | APPUSER_PASSWORD | ""           | ""
        ""            | APPUSER_PASSWORD | ""           | APPUSER_BARCODE
        ""            | APPUSER_PASSWORD | APPUSER_NAME | null
        ""            | APPUSER_PASSWORD | APPUSER_NAME | ""
        ""            | APPUSER_PASSWORD | APPUSER_NAME | APPUSER_BARCODE
        APPUSER_EMAIL | null             | null         | null
        APPUSER_EMAIL | null             | null         | ""
        APPUSER_EMAIL | null             | null         | APPUSER_BARCODE
        APPUSER_EMAIL | null             | ""           | null
        APPUSER_EMAIL | null             | ""           | ""
        APPUSER_EMAIL | null             | ""           | APPUSER_BARCODE
        APPUSER_EMAIL | null             | APPUSER_NAME | null
        APPUSER_EMAIL | null             | APPUSER_NAME | ""
        APPUSER_EMAIL | null             | APPUSER_NAME | APPUSER_BARCODE
        APPUSER_EMAIL | ""               | null         | null
        APPUSER_EMAIL | ""               | null         | ""
        APPUSER_EMAIL | ""               | null         | APPUSER_BARCODE
        APPUSER_EMAIL | ""               | ""           | null
        APPUSER_EMAIL | ""               | ""           | ""
        APPUSER_EMAIL | ""               | ""           | APPUSER_BARCODE
        APPUSER_EMAIL | ""               | APPUSER_NAME | null
        APPUSER_EMAIL | ""               | APPUSER_NAME | ""
        APPUSER_EMAIL | ""               | APPUSER_NAME | APPUSER_BARCODE
    }

//    MockHttpServletResponse doPost(String url, JSONObject requestBody = new JSONObject()) {
//        MockHttpServletResponse httpServletResponse = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post(url)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Auth-Token", token)
//                        .content(requestBody.toString())
//                )
//                .andReturn()
//                .response
//
//        assert MediaType.parseMediaType(httpServletResponse.contentType) == MediaType.APPLICATION_JSON
//
//        return httpServletResponse
//    }
//
//    def "get"() {
//        given:
//        def requestBody = new JSONObject()
//        requestBody.put("id", 1)
//
//        when:
//        def rawResponse = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/api/v1/public/appUser/get")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Auth-Token", token)
//                        .content(requestBody.toString())
//                )
//                .andReturn()
//                .response
//
//        then:
//        rawResponse.status == HttpStatus.OK.value()
//        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON
//
//        when:
//        JSONObject response = new JSONObject(rawResponse.contentAsString)
//
//        then:
//        validateResponseAsJsonResponse(response)
//
//        response.getBoolean("success")
//
//        when:
//        JSONObject appUser = response.getJSONObject("data")
//
//        then:
//        !appUser.has("password")
//
//        appUser.has("id")
//        appUser.get("id") instanceof Integer
//
//        appUser.has("email")
//        appUser.get("email") instanceof String
//
//        appUser.has("name")
//        appUser.get("name") == JSONObject.NULL
//
//        appUser.has("appUserRoles")
//
//        when:
//        JSONArray appUserRoles = appUser.getJSONArray("appUserRoles")
//
//        then:
//        appUserRoles.length() == Role.values().length
//
//        for (int i = 0; i < appUserRoles.length(); i++) {
//            Role role = Role.valueOf(appUserRoles.getString(i))
//            assert Role.values().contains(role)
//        }
//    }
//
//    def "save"() {
//        setup:
//        Long appUserId = 'retrieve test appUser ID and creates if not exists'()
//
//        expect:
//        appUserId != null
//
//        when:
//        JSONObject requestBody = new JSONObject()
//        requestBody.put("id", appUserId)
//        requestBody.put("name", APPUSER_NAME_NEW)
//        requestBody.put("appUserRoles", new JSONArray(APPUSER_ROLES_NEW))
//
//        def rawResponse = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/api/v1/public/appUser/save")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Auth-Token", token)
//                        .content(requestBody.toString())
//                )
//                .andReturn()
//                .response
//
//        then:
//        rawResponse.status == HttpStatus.OK.value()
//        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON
//
//        when:
//        JSONObject response = new JSONObject(rawResponse.contentAsString)
//
//        then:
//        validateResponseAsJsonResponse(response)
//
//        response.getBoolean("success")
//
//        when:
//        JSONObject appUser = response.getJSONObject("data")
//
//        then:
//        !appUser.has("password")
//
//        appUser.has("id")
//        appUser.get("id") instanceof Integer
//        appUser.getLong("id") == appUserId
//
//        appUser.has("email")
//        appUser.get("email") instanceof String
//        appUser.getString("email") == APPUSER_EMAIL
//
//        appUser.has("name")
//        appUser.get("name") instanceof String
//        appUser.getString("name") == APPUSER_NAME_NEW
//
//        appUser.has("appUserRoles")
//
//        when:
//        JSONArray appUserRoles = appUser.getJSONArray("appUserRoles")
//
//        then:
//        appUserRoles.length() == APPUSER_ROLES_NEW.length
//
//        "check number of appUsers in database"() == 2
//    }
//
//    def "delete"() {
//        setup:
//        Long appUserId = 'retrieve test appUser ID and creates if not exists'()
//
//        expect:
//        appUserId != null
//
//        when:
//        JSONObject requestBody = new JSONObject()
//        requestBody.put("id", appUserId)
//
//        def rawResponse = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/api/v1/public/appUser/delete")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Auth-Token", token)
//                        .content(requestBody.toString())
//                )
//                .andReturn()
//                .response
//
//        then:
//        rawResponse.status == HttpStatus.OK.value()
//        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON
//
//        when:
//        JSONObject response = new JSONObject(rawResponse.contentAsString)
//
//        then:
//        validateResponseAsJsonResponse(response)
//
//        response.getBoolean("success")
//
//        "check number of appUsers in database"() == 1
//    }
//
//    def "getAll"() {
//        when:
//        def rawResponse = doPost("/api/v1/public/appUser/getAll")
//
//        then:
//        rawResponse.status == HttpStatus.OK.value()
//
//        when:
//        JSONObject response = new JSONObject(rawResponse.contentAsString)
//
//        then:
//        validateResponseAsJsonResponse(response)
//
//        response.getBoolean("success")
//
//        when:
//        JSONArray appUsers = response.getJSONArray("data")
//
//        then:
//        appUsers.length() == 1
//
//        when:
//        JSONObject appUser = appUsers.getJSONObject(0)
//
//        then:
//        appUser.has("id")
//        appUser.get("id") instanceof Integer
//
//        appUser.has("email")
//        appUser.get("email") instanceof String
//
//        appUser.has("name")
//        appUser.get("name") == JSONObject.NULL
//    }
//
//    def "login"() {
//        given:
//        JSONObject requestBody = new JSONObject()
//        requestBody.put("email", "admin")
//        requestBody.put("password", "admin")
//
//        when:
//        def rawResponse = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/api/v1/public/appUser/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody.toString())
//                )
//                .andReturn()
//                .response
//
//        then:
//        rawResponse.status == HttpStatus.OK.value()
//        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON
//
//        when:
//        JSONObject response = new JSONObject(rawResponse.contentAsString)
//
//        then:
//        validateResponseAsJsonResponse(response)
//
//        response.getBoolean("success")
//
//        when:
//        JSONObject data = response.getJSONObject("data")
//
//        then:
//        data.has("expiration")
//        new DateTime(data.getLong("expiration")).isAfterNow()
//
//        data.has("token")
//        data.getString("token").length() == TokenUtility.LENGTH
//    }
//
//    def "changePassword"() {
//        setup:
//        Long appUserId = "retrieve test appUser ID and creates if not exists"()
//        String appUserToken = "get appUser token via login"()
//
//        expect:
//        appUserId != null
//        appUserToken != null
//
//        when: "Token of appUser 'admin'"
//        JSONObject requestBody = new JSONObject()
//        requestBody.put("oldPassword", APPUSER_PASSWORD)
//        requestBody.put("newPassword", APPUSER_PASSWORD_NEW)
//
//        def rawResponse = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/api/v1/public/appUser/changePassword")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Auth-Token", token)
//                        .content(requestBody.toString())
//                )
//                .andReturn()
//                .response
//
//        then:
//        rawResponse.status == HttpStatus.OK.value()
//        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON
//
//        when:
//        JSONObject response = new JSONObject(rawResponse.contentAsString)
//
//        then:
//        validateResponseAsJsonResponse(response)
//
//        !response.getBoolean("success")
//
//        when:
//        "Token of appUser '${APPUSER_NAME}'"
//        requestBody = new JSONObject()
//        requestBody.put("oldPassword", APPUSER_PASSWORD)
//        requestBody.put("newPassword", APPUSER_PASSWORD_NEW)
//
//        rawResponse = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/api/v1/public/appUser/changePassword")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Auth-Token", appUserToken)
//                        .content(requestBody.toString())
//                )
//                .andReturn()
//                .response
//
//        then:
//        rawResponse.status == HttpStatus.OK.value()
//        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON
//
//        when:
//        response = new JSONObject(rawResponse.contentAsString)
//
//        then:
//        validateResponseAsJsonResponse(response)
//
//        response.getBoolean("success")
//    }
//
//    private Long 'retrieve test appUser ID and creates if not exists'() {
//        AppUser appUser = appUserService.findByEmail(APPUSER_EMAIL)
//
//        if (appUser != null) {
//            appUserService.delete(appUser.id)
//            appUser = null
//        }
//
//        if (appUser == null) {
//            appUser = appUserService.create(APPUSER_EMAIL, APPUSER_PASSWORD, APPUSER_NAME)
//        }
//
//        appUser = appUserService.update(appUser.id, null, APPUSER_ROLES_NEW)
//
//        return appUser.id
//    }
//
//    private String "get appUser token via login"() {
//        JSONObject requestBody = new JSONObject()
//        requestBody.put("email", APPUSER_EMAIL)
//        requestBody.put("password", APPUSER_PASSWORD)
//
//        def rawResponse = mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/api/v1/public/appUser/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody.toString())
//                )
//                .andReturn()
//                .response
//
//        rawResponse.status == HttpStatus.OK.value()
//        JSONObject response = new JSONObject(rawResponse.contentAsString)
//
//        if (!response.getBoolean("success")) {
//            return null
//        }
//
//        JSONObject data = response.getJSONObject("data")
//
//        return data.getString("token")
//    }

    private int "check number of appUsers in database"() {
        return appUserService.getAll().size()
    }
}
