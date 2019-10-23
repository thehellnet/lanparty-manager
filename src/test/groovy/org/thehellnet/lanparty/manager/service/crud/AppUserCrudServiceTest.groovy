package org.thehellnet.lanparty.manager.service.crud

import org.springframework.beans.factory.annotation.Autowired
import org.thehellnet.lanparty.manager.exception.controller.AlreadyPresentException
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException
import org.thehellnet.lanparty.manager.model.constant.Role
import org.thehellnet.lanparty.manager.model.dto.service.AppUserServiceDTO
import org.thehellnet.lanparty.manager.model.persistence.AppUser
import org.thehellnet.lanparty.manager.repository.AppUserRepository
import org.thehellnet.lanparty.manager.repository.AppUserTokenRepository
import org.thehellnet.utility.PasswordUtility
import spock.lang.Unroll

class AppUserCrudServiceTest extends CrudServiceSpecification {

    private static final String APPUSER_EMAIL = "user@domain.tdl"
    private static final String APPUSER_NAME = "User"
    private static final String APPUSER_PASSWORD = "password"
    private static final String APPUSER_BARCODE = "0123456789"

    private static final String APPUSER_NAME_NEW = "New name"
    private static final String APPUSER_PASSWORD_NEW = "passwordnew"
    private static final String APPUSER_BARCODE_NEW = "9876543210"

    @Autowired
    private AppUserRepository appUserRepository

    @Autowired
    private AppUserTokenRepository appUserTokenRepository

    @Autowired
    private AppUserCrudService appUserCrudService

    @Unroll
    def "create valid user with \"#email\" \"#password\" \"#name\" \"#barcode\""(String email, String password, String name, String barcode) {
        when:
        AppUserServiceDTO serviceDTO = new AppUserServiceDTO(
                email: email,
                password: password,
                name: name,
                barcode: barcode
        )
        AppUser appUser = appUserCrudService.create(serviceDTO)

        then:
        appUser != null
        appUser.id != null
        appUser.email == email
        appUser.name == name
        appUser.barcode == barcode
        appUser.appUserRoles.size() == 0

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


    def "create with invalid email"(String email, String password) {
        when:
        AppUserServiceDTO serviceDTO = new AppUserServiceDTO(
                email: email,
                password: password,
                name: APPUSER_NAME,
                barcode: APPUSER_BARCODE
        )
        appUserCrudService.create(serviceDTO)

        then:
        thrown InvalidDataException

        where:
        email             | password
        null              | null
        null              | ""
        ""                | null
        ""                | ""
        "not_valid_email" | null
        "not_valid_email" | ""
    }

    def "create with already exiting email"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, APPUSER_PASSWORD))

        when:
        AppUserServiceDTO serviceDTO = new AppUserServiceDTO(
                email: APPUSER_EMAIL,
                password: APPUSER_PASSWORD,
                name: APPUSER_NAME,
                barcode: APPUSER_BARCODE
        )
        appUserCrudService.create(serviceDTO)

        then:
        thrown AlreadyPresentException
    }

    @Unroll
    def "create with \"#password\" password"(String password) {
        when:
        AppUserServiceDTO serviceDTO = new AppUserServiceDTO(
                email: APPUSER_EMAIL,
                password: password,
                name: APPUSER_NAME,
                barcode: APPUSER_BARCODE
        )
        appUserCrudService.create(serviceDTO)

        then:
        thrown InvalidDataException

        where:
        password << [null, ""]
    }

    def "get with existing id"() {
        given:
        Long userId = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD))).id

        when:
        AppUser appUser = appUserCrudService.read(userId)

        then:
        appUser != null
        appUser.id == userId
        appUser.email == APPUSER_EMAIL
        appUser.name == null
        appUser.barcode == null
        appUser.appUserRoles.size() == 0
    }

    def "get with not existing id"() {
        given:
        Long appUserId = 12345678

        when:
        appUserCrudService.read(appUserId)

        then:
        thrown NotFoundException
    }

    def "getAll with admin user only"() {
        when:
        List<AppUser> appUsers = appUserCrudService.readAll()

        then:
        appUsers.size() == 1

        when:
        AppUser appUser = appUsers.get(0)

        then:
        appUser != null
        appUser.email == "admin"
    }

    def "getAll with two users"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD)))

        when:
        List<AppUser> appUsers = appUserCrudService.readAll()

        then:
        appUsers.size() == 2

        when:
        AppUser appUser = appUsers.get(1)

        then:
        appUser != null
        appUser.email == APPUSER_EMAIL
        appUser.appUserRoles.size() == 0
    }

    def "getAll with no users"() {
        given:
        appUserRepository.deleteAll()

        when:
        List<AppUser> appUsers = appUserCrudService.readAll()

        then:
        appUsers.size() == 0
    }

    @Unroll
    def "update UnchangedException #name name, #password password and #appUserRoles appUserRoles"(String name, String password, String[] appUserRoles, String barcode) {
        given:
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME, APPUSER_BARCODE)).id

        when:
        AppUserServiceDTO serviceDTO = new AppUserServiceDTO(
                name: name,
                password: password,
                appUserRoles: appUserRoles,
                barcode: barcode
        )
        appUserCrudService.update(appUserId, serviceDTO)

        then:
        thrown UnchangedException

        where:
        name | password | appUserRoles | barcode
        null | null     | null         | null
        null | ""       | null         | null
    }

    @Unroll
    def "update with \"#name\" name, \"#password\" password, \"#appUserRoles\" appUserRolesand \"#barcode\" barcode"(String name, String password, String[] appUserRoles, String barcode) {
        given:
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME, APPUSER_BARCODE)).id

        when:
        AppUserServiceDTO serviceDTO = new AppUserServiceDTO(
                name: name,
                password: password,
                appUserRoles: appUserRoles,
                barcode: barcode
        )
        appUserCrudService.update(appUserId, serviceDTO)

        and:
        AppUser appUser = appUserRepository.getOne(appUserId)

        then:
        appUser != null
        appUser.id == appUserId
        appUser.email == APPUSER_EMAIL
        PasswordUtility.verify(appUser.password, ((password != null && password.length() > 0) ? password : APPUSER_PASSWORD))
        appUser.name == (name != null ? name : APPUSER_NAME)
        appUser.barcode == (barcode != null ? barcode : APPUSER_BARCODE)
        appUser.appUserRoles.size() == (appUserRoles != null ? appUserRoles.length : 0)

        where:
        name             | password             | appUserRoles                         | barcode
        null             | null                 | [] as String[]                       | null
        null             | null                 | [] as String[]                       | ""
        null             | null                 | [] as String[]                       | APPUSER_BARCODE_NEW
        null             | null                 | [Role.ACTION_LOGIN.name] as String[] | null
        null             | null                 | [Role.ACTION_LOGIN.name] as String[] | ""
        null             | null                 | [Role.ACTION_LOGIN.name] as String[] | APPUSER_BARCODE_NEW
        null             | ""                   | [] as String[]                       | null
        null             | ""                   | [] as String[]                       | ""
        null             | ""                   | [] as String[]                       | APPUSER_BARCODE_NEW
        null             | ""                   | [Role.ACTION_LOGIN.name] as String[] | null
        null             | ""                   | [Role.ACTION_LOGIN.name] as String[] | ""
        null             | ""                   | [Role.ACTION_LOGIN.name] as String[] | APPUSER_BARCODE_NEW
        null             | APPUSER_PASSWORD_NEW | null                                 | null
        null             | APPUSER_PASSWORD_NEW | null                                 | ""
        null             | APPUSER_PASSWORD_NEW | null                                 | APPUSER_BARCODE_NEW
        null             | APPUSER_PASSWORD_NEW | [] as String[]                       | null
        null             | APPUSER_PASSWORD_NEW | [] as String[]                       | ""
        null             | APPUSER_PASSWORD_NEW | [] as String[]                       | APPUSER_BARCODE_NEW
        null             | APPUSER_PASSWORD_NEW | [Role.ACTION_LOGIN.name] as String[] | null
        null             | APPUSER_PASSWORD_NEW | [Role.ACTION_LOGIN.name] as String[] | ""
        null             | APPUSER_PASSWORD_NEW | [Role.ACTION_LOGIN.name] as String[] | APPUSER_BARCODE_NEW
        ""               | null                 | [] as String[]                       | null
        ""               | null                 | [] as String[]                       | ""
        ""               | null                 | [] as String[]                       | APPUSER_BARCODE_NEW
        ""               | null                 | [Role.ACTION_LOGIN.name] as String[] | null
        ""               | null                 | [Role.ACTION_LOGIN.name] as String[] | ""
        ""               | null                 | [Role.ACTION_LOGIN.name] as String[] | APPUSER_BARCODE_NEW
        ""               | ""                   | [] as String[]                       | null
        ""               | ""                   | [] as String[]                       | ""
        ""               | ""                   | [] as String[]                       | APPUSER_BARCODE_NEW
        ""               | ""                   | [Role.ACTION_LOGIN.name] as String[] | null
        ""               | ""                   | [Role.ACTION_LOGIN.name] as String[] | ""
        ""               | ""                   | [Role.ACTION_LOGIN.name] as String[] | APPUSER_BARCODE_NEW
        ""               | APPUSER_PASSWORD_NEW | null                                 | null
        ""               | APPUSER_PASSWORD_NEW | null                                 | ""
        ""               | APPUSER_PASSWORD_NEW | null                                 | APPUSER_BARCODE_NEW
        ""               | APPUSER_PASSWORD_NEW | [] as String[]                       | null
        ""               | APPUSER_PASSWORD_NEW | [] as String[]                       | ""
        ""               | APPUSER_PASSWORD_NEW | [] as String[]                       | APPUSER_BARCODE_NEW
        ""               | APPUSER_PASSWORD_NEW | [Role.ACTION_LOGIN.name] as String[] | null
        ""               | APPUSER_PASSWORD_NEW | [Role.ACTION_LOGIN.name] as String[] | ""
        ""               | APPUSER_PASSWORD_NEW | [Role.ACTION_LOGIN.name] as String[] | APPUSER_BARCODE_NEW
        APPUSER_NAME_NEW | null                 | null                                 | null
        APPUSER_NAME_NEW | null                 | null                                 | ""
        APPUSER_NAME_NEW | null                 | null                                 | APPUSER_BARCODE_NEW
        APPUSER_NAME_NEW | null                 | [] as String[]                       | null
        APPUSER_NAME_NEW | null                 | [] as String[]                       | ""
        APPUSER_NAME_NEW | null                 | [] as String[]                       | APPUSER_BARCODE_NEW
        APPUSER_NAME_NEW | null                 | [Role.ACTION_LOGIN.name] as String[] | null
        APPUSER_NAME_NEW | null                 | [Role.ACTION_LOGIN.name] as String[] | ""
        APPUSER_NAME_NEW | null                 | [Role.ACTION_LOGIN.name] as String[] | APPUSER_BARCODE_NEW
        APPUSER_NAME_NEW | ""                   | null                                 | null
        APPUSER_NAME_NEW | ""                   | null                                 | ""
        APPUSER_NAME_NEW | ""                   | null                                 | APPUSER_BARCODE_NEW
        APPUSER_NAME_NEW | ""                   | [] as String[]                       | null
        APPUSER_NAME_NEW | ""                   | [] as String[]                       | ""
        APPUSER_NAME_NEW | ""                   | [] as String[]                       | APPUSER_BARCODE_NEW
        APPUSER_NAME_NEW | ""                   | [Role.ACTION_LOGIN.name] as String[] | null
        APPUSER_NAME_NEW | ""                   | [Role.ACTION_LOGIN.name] as String[] | ""
        APPUSER_NAME_NEW | ""                   | [Role.ACTION_LOGIN.name] as String[] | APPUSER_BARCODE_NEW
        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | null                                 | null
        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | null                                 | ""
        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | null                                 | APPUSER_BARCODE_NEW
        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | [] as String[]                       | null
        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | [] as String[]                       | ""
        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | [] as String[]                       | APPUSER_BARCODE_NEW
        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | [Role.ACTION_LOGIN.name] as String[] | null
        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | [Role.ACTION_LOGIN.name] as String[] | ""
        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | [Role.ACTION_LOGIN.name] as String[] | APPUSER_BARCODE_NEW
    }

    def "update with invalid id"() {
        given:
        Long appUserId = 12345678

        when:
        AppUserServiceDTO serviceDTO = new AppUserServiceDTO(
                name: APPUSER_NAME_NEW,
                password: APPUSER_PASSWORD_NEW,
                appUserRoles: [Role.ACTION_LOGIN.name] as String[],
                barcode: APPUSER_BARCODE_NEW
        )
        appUserCrudService.update(appUserId, serviceDTO)

        then:
        thrown NotFoundException
    }

    def "delete"() {
        given:
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD))).id

        when:
        appUserCrudService.delete(appUserId)

        then:
        noExceptionThrown()

        and:
        appUserRepository.findAll().size() == 1
    }

    def "delete with not existing ID"() {
        given:
        Long appUserId = 12345678

        when:
        appUserCrudService.delete(appUserId)

        then:
        thrown NotFoundException
    }
}
