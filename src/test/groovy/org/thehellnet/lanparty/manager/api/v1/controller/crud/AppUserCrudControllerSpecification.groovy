package org.thehellnet.lanparty.manager.api.v1.controller.crud

import org.json.JSONArray
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.thehellnet.lanparty.manager.api.v1.controller.ControllerSpecification
import org.thehellnet.lanparty.manager.model.constant.Role
import org.thehellnet.lanparty.manager.model.persistence.AppUser
import org.thehellnet.utility.PasswordUtility
import spock.lang.Unroll

class AppUserCrudControllerSpecification extends ControllerSpecification {


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
        requestBody.put("appUserRoles", new String[0])
        requestBody.put("barcode", barcode)

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/public/v1/crud/appUser")
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
        requestBody.put("appUserRoles", new String[0])
        requestBody.put("barcode", barcode)

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/public/v1/crud/appUser")
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

    @Unroll
    def "create error with '#value' values #email #password #name #barcode"(boolean email, boolean password, boolean name, boolean barcode, String value) {
        given:
        def requestBody = new JSONObject()
        if (email) requestBody.put("email", value)
        if (password) requestBody.put("password", value)
        if (name) requestBody.put("name", value)
        if (barcode) requestBody.put("barcode", value)

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/public/v1/crud/appUser")
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
        [email, password, name, barcode, value] << [
                [false, true],
                [false, true],
                [false, true],
                [false, true],
                [null, ""]
        ].combinations()
    }

    @Unroll
    def "create error with valid values #email #password #name #barcode"(boolean email, boolean password, boolean name, boolean barcode) {
        given:
        def requestBody = new JSONObject()
        if (email) requestBody.put("email", APPUSER_EMAIL)
        if (password) requestBody.put("password", APPUSER_PASSWORD)
        if (name) requestBody.put("name", APPUSER_NAME)
        if (barcode) requestBody.put("barcode", APPUSER_BARCODE)

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/public/v1/crud/appUser")
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
        email | password | name  | barcode
        false | false    | false | false
        false | false    | false | true
        false | false    | true  | false
        false | false    | true  | true
        false | true     | false | false
        false | true     | false | true
        false | true     | true  | false
        false | true     | true  | true
        true  | false    | false | false
        true  | false    | false | true
        true  | false    | true  | false
        true  | false    | true  | true
    }

    def "readAll with one user"() {
        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/public/v1/crud/appUser")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        when:
        JSONArray appUsers = new JSONArray(rawResponse.contentAsString)

        then:
        appUsers.length() == 1

        when:
        JSONObject appUser = appUsers.getJSONObject(0)

        then:
        appUser.has("id")
        appUser.get("id") instanceof Integer

        appUser.has("email")
        appUser.get("email") instanceof String
        appUser.getString("email") == "admin"
    }

    def "readAll with more than one user"() {
        given:
        AppUser appUser1 = new AppUser(
                email: APPUSER_EMAIL,
                password: PasswordUtility.hash(APPUSER_PASSWORD),
                name: APPUSER_NAME,
                barcode: APPUSER_BARCODE
        )
        appUserRepository.save(appUser1)

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/public/v1/crud/appUser")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        when:
        JSONArray appUsers = new JSONArray(rawResponse.contentAsString)

        then:
        appUsers.length() == 2

        when:
        JSONObject appUser = appUsers.getJSONObject(0)

        then:
        appUser.has("id")
        appUser.get("id") instanceof Integer

        appUser.has("email")
        appUser.get("email") instanceof String
        appUser.getString("email") == "admin"

        when:
        appUser = appUsers.getJSONObject(1)

        then:
        appUser.has("id")
        appUser.get("id") instanceof Integer

        appUser.has("email")
        appUser.get("email") instanceof String
        appUser.getString("email") == APPUSER_EMAIL
    }

    def "read with id == 1"() {
        given:
        Long appUserId = 1

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/public/v1/crud/appUser/${appUserId}")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        when:
        JSONObject appUser = new JSONObject(rawResponse.contentAsString)

        then:
        appUser.has("id")
        appUser.get("id") instanceof Integer
        appUser.getLong("id") == appUserId
    }

    def "read with new appUser created"() {
        given:
        AppUser appUser1 = new AppUser(
                email: APPUSER_EMAIL,
                password: PasswordUtility.hash(APPUSER_PASSWORD),
                name: APPUSER_NAME,
                barcode: APPUSER_BARCODE
        )
        Long appUserId = appUserRepository.save(appUser1).id

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/public/v1/crud/appUser/${appUserId}")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        when:
        JSONObject appUser = new JSONObject(rawResponse.contentAsString)

        then:
        appUser.has("id")
        appUser.get("id") instanceof Integer
        appUser.getLong("id") == appUserId

        appUser.has("name")
        appUser.get("name") instanceof String
        appUser.getString("name") == APPUSER_NAME

        appUser.has("barcode")
        appUser.get("barcode") instanceof String
        appUser.getString("barcode") == APPUSER_BARCODE
    }

    def "update without id"() {
        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/api/public/v1/crud/appUser")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.METHOD_NOT_ALLOWED.value()
    }

    def "update with wrong appUserId"() {
        given:
        Long appUserId = 123
        JSONObject requestBody = new JSONObject()

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/api/public/v1/crud/appUser/${appUserId}")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.NOT_FOUND.value()
    }

    def "update with empty body"() {
        given:
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_NAME, APPUSER_PASSWORD)).id
        JSONObject requestBody = new JSONObject()

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/api/public/v1/crud/appUser/${appUserId}")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.NO_CONTENT.value()
    }

    def "update with email"() {
        given:
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_NAME, APPUSER_PASSWORD, APPUSER_NAME, [] as Set<Role>, APPUSER_BARCODE)).id
        JSONObject requestBody = new JSONObject()
        requestBody.put("email", APPUSER_EMAIL_NEW)

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/api/public/v1/crud/appUser/${appUserId}")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        when:
        JSONObject appUser = new JSONObject(rawResponse.contentAsString)

        then:
        appUser.getLong("id") == appUserId
        appUser.getString("email") == APPUSER_EMAIL_NEW
        appUser.getString("name") == APPUSER_NAME
        appUser.getString("barcode") == APPUSER_BARCODE
    }

    def "update with name"() {
        given:
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_NAME, APPUSER_PASSWORD, APPUSER_NAME, [] as Set<Role>, APPUSER_BARCODE)).id
        JSONObject requestBody = new JSONObject()
        requestBody.put("name", APPUSER_NAME_NEW)

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/api/public/v1/crud/appUser/${appUserId}")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        when:
        JSONObject appUser = new JSONObject(rawResponse.contentAsString)

        then:
        appUser.getLong("id") == appUserId
        appUser.getString("name") == APPUSER_NAME_NEW
        appUser.getString("barcode") == APPUSER_BARCODE
    }

    def "update with barcode"() {
        given:
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_NAME, APPUSER_PASSWORD, APPUSER_NAME, [] as Set<Role>, APPUSER_BARCODE)).id
        JSONObject requestBody = new JSONObject()
        requestBody.put("barcode", APPUSER_BARCODE_NEW)

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/api/public/v1/crud/appUser/${appUserId}")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        when:
        JSONObject appUser = new JSONObject(rawResponse.contentAsString)

        then:
        appUser.getLong("id") == appUserId
        appUser.getString("name") == APPUSER_NAME
        appUser.getString("barcode") == APPUSER_BARCODE_NEW
    }

    def "update with name and barcode"() {
        given:
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_NAME, APPUSER_PASSWORD, APPUSER_NAME, [] as Set<Role>, APPUSER_BARCODE)).id
        JSONObject requestBody = new JSONObject()
        requestBody.put("name", APPUSER_NAME_NEW)
        requestBody.put("barcode", APPUSER_BARCODE_NEW)

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/api/public/v1/crud/appUser/${appUserId}")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.OK.value()
        MediaType.parseMediaType(rawResponse.contentType) == MediaType.APPLICATION_JSON

        when:
        JSONObject appUser = new JSONObject(rawResponse.contentAsString)

        then:
        appUser.getLong("id") == appUserId
        appUser.getString("name") == APPUSER_NAME_NEW
        appUser.getString("barcode") == APPUSER_BARCODE_NEW
    }

    def "delete without id"() {
        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/public/v1/crud/appUser")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.METHOD_NOT_ALLOWED.value()
    }

    def "delete with wrong appUserId"() {
        given:
        Long appUserId = 123
        JSONObject requestBody = new JSONObject()

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/public/v1/crud/appUser/${appUserId}")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.NOT_FOUND.value()
    }

    def "delete with valid appUserId"() {
        given:
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_NAME, APPUSER_PASSWORD)).id
        JSONObject requestBody = new JSONObject()

        when:
        def rawResponse = mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/public/v1/crud/appUser/${appUserId}")
                        .header("X-Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andReturn()
                .response

        then:
        rawResponse.status == HttpStatus.NO_CONTENT.value()

        and:
        "check number of appUsers in database"() == 1
    }

    private int "check number of appUsers in database"() {
        return appUserRepository.findAll().size()
    }
}
