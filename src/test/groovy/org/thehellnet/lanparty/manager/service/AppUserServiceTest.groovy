package org.thehellnet.lanparty.manager.service

import org.springframework.beans.factory.annotation.Autowired
import org.thehellnet.lanparty.manager.exception.appuser.AppUserAlreadyPresentException
import org.thehellnet.lanparty.manager.exception.appuser.AppUserInvalidMailException
import org.thehellnet.lanparty.manager.exception.appuser.AppUserNotFoundException
import org.thehellnet.lanparty.manager.model.constant.Role
import org.thehellnet.lanparty.manager.model.persistence.AppUser
import org.thehellnet.lanparty.manager.repository.AppUserRepository
import org.thehellnet.lanparty.manager.repository.AppUserTokenRepository
import org.thehellnet.utility.PasswordUtility
import spock.lang.Unroll

class AppUserServiceTest extends ServiceSpecification {

    private static final String APPUSER_EMAIL = "user@domain.tdl"
    private static final String APPUSER_NAME = "User"
    private static final String APPUSER_PASSWORD = "password"

    private static final String APPUSER_NAME_NEW = "New name"
    private static final String APPUSER_PASSWORD_NEW = "passwordnew"

    @Autowired
    private AppUserRepository appUserRepository

    @Autowired
    private AppUserTokenRepository appUserTokenRepository

    @Autowired
    private AppUserService appUserService

    def "getAll with admin user only"() {
        when:
        List<AppUser> appUsers = appUserService.getAll()

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
        List<AppUser> appUsers = appUserService.getAll()

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
        List<AppUser> appUsers = appUserService.getAll()

        then:
        appUsers.size() == 0
    }

    def "get with existing id"() {
        given:
        Long userId = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD))).id

        when:
        AppUser appUser = appUserService.get(userId)

        then:
        appUser != null
        appUser.id == userId
        appUser.email == APPUSER_EMAIL
        appUser.name == null
        appUser.appUserRoles.size() == 0
    }

    def "get with not existing id"() {
        given:
        Long appUserId = 12345678

        when:
        AppUser appUser = appUserService.get(appUserId)

        then:
        appUser == null
    }

    def "create normal user"() {
        when:
        AppUser appUser = appUserService.create(APPUSER_EMAIL, APPUSER_PASSWORD, APPUSER_NAME)

        then:
        appUser != null
        appUser.id != null
        appUser.email == APPUSER_EMAIL
        appUser.name == APPUSER_NAME
        appUser.appUserRoles.size() == 0
    }

    def "create normal user with null name"() {
        when:
        AppUser appUser = appUserService.create(APPUSER_EMAIL, APPUSER_PASSWORD, null)

        then:
        appUser != null
        appUser.id != null
        appUser.email == APPUSER_EMAIL
        appUser.name == null
        appUser.appUserRoles.size() == 0
    }

    def "create normal user with empty name"() {
        when:
        AppUser appUser = appUserService.create(APPUSER_EMAIL, APPUSER_PASSWORD, "")

        then:
        appUser != null
        appUser.id != null
        appUser.email == APPUSER_EMAIL
        appUser.name == null
        appUser.appUserRoles.size() == 0
    }

    def "create with invalid email"() {
        when:
        appUserService.create("not_valid_email", APPUSER_PASSWORD, APPUSER_NAME)

        then:
        thrown AppUserInvalidMailException
    }

    def "create with already exiting email"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, APPUSER_PASSWORD))

        when:
        appUserService.create(APPUSER_EMAIL, APPUSER_PASSWORD, APPUSER_NAME)

        then:
        thrown AppUserAlreadyPresentException
    }

    def "save with null values"() {
        given:
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)).id

        when:
        appUserService.save(appUserId, null, null)

        and:
        AppUser appUser = appUserRepository.getOne(appUserId)

        then:
        appUser != null
        appUser.id == appUserId
        appUser.email == APPUSER_EMAIL
        appUser.name == APPUSER_NAME
        appUser.appUserRoles.size() == 0
    }

    def "save with empty name and null appUserRoles"() {
        given:
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)).id

        when:
        appUserService.save(appUserId, "", null)

        and:
        AppUser appUser = appUserRepository.getOne(appUserId)

        then:
        appUser != null
        appUser.id == appUserId
        appUser.email == APPUSER_EMAIL
        appUser.name == APPUSER_NAME
        appUser.appUserRoles.size() == 0
    }

    def "save with new name"() {
        given:
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)).id

        when:
        appUserService.save(appUserId, APPUSER_NAME_NEW, null)

        and:
        AppUser appUser = appUserRepository.getOne(appUserId)

        then:
        appUser != null
        appUser.id == appUserId
        appUser.email == APPUSER_EMAIL
        appUser.appUserRoles.size() == 0

        and:
        appUser.name == APPUSER_NAME_NEW
    }

    def "save with new appUserRoles"() {
        given:
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)).id

        when:
        appUserService.save(appUserId, null, [Role.LOGIN.name] as String[])

        and:
        AppUser appUser = appUserRepository.getOne(appUserId)

        then:
        appUser != null
        appUser.id == appUserId
        appUser.name == APPUSER_NAME
        appUser.email == APPUSER_EMAIL

        and:
        appUser.appUserRoles.size() == 1
        appUser.appUserRoles.contains(Role.LOGIN)
    }

    def "save with both new name and new appUserRoles"() {
        given:
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)).id

        when:
        appUserService.save(appUserId, APPUSER_NAME_NEW, [Role.LOGIN.name] as String[])

        and:
        AppUser appUser = appUserRepository.getOne(appUserId)

        then:
        appUser != null
        appUser.id == appUserId
        appUser.email == APPUSER_EMAIL

        and:
        appUser.name == APPUSER_NAME_NEW
        appUser.appUserRoles.size() == 1
        appUser.appUserRoles.contains(Role.LOGIN)
    }

    def "save with invalid id"() {
        given:
        Long appUserId = 12345678

        when:
        appUserService.save(appUserId, APPUSER_NAME_NEW, [Role.LOGIN.name] as String[])

        then:
        thrown AppUserNotFoundException
    }

    def "delete"() {
        given:
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)).id

        when:
        appUserService.delete(appUserId)

        then:
        noExceptionThrown()
    }

    def "delete with not existing ID"() {
        given:
        Long appUserId = 12345678

        when:
        appUserService.delete(appUserId)

        then:
        thrown AppUserNotFoundException
    }

    def "findByEmail with admin"() {
        given:
        String email = "admin"

        when:
        AppUser appUser = appUserService.findByEmail(email)

        then:
        appUser != null
        appUser.email == "admin"
    }

    def "findByEmail with exiting email"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME))

        when:
        AppUser appUser = appUserService.findByEmail(APPUSER_EMAIL)

        then:
        appUser != null
        appUser.email == APPUSER_EMAIL
    }

    def "findByEmail with not exiting email"() {
        given:
        String email = APPUSER_EMAIL

        when:
        AppUser appUser = appUserService.findByEmail(email)

        then:
        appUser == null
    }

    def "findByEmail with not valid email"() {
        given:
        String email = "not_valid_email"

        when:
        AppUser appUser = appUserService.findByEmail(email)

        then:
        appUser == null
    }

    def "findByEmailAndPassword with admin user"() {
        given:
        String email = "admin"
        String password = "admin"

        when:
        AppUser appUser = appUserService.findByEmailAndPassword(email, password)

        then:
        appUser != null
        appUser.email == "admin"
    }

    def "findByEmailAndPassword with exiting user"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME))

        when:
        AppUser appUser = appUserService.findByEmailAndPassword(APPUSER_EMAIL, APPUSER_PASSWORD)

        then:
        appUser != null
        appUser.email == APPUSER_EMAIL
        appUser.name == APPUSER_NAME
    }

    def "findByEmailAndPassword with not exiting user"() {
        given:
        String email = APPUSER_EMAIL
        String password = APPUSER_PASSWORD

        when:
        AppUser appUser = appUserService.findByEmailAndPassword(email, password)

        then:
        appUser == null
    }

    @Unroll
    def "findByEmailAndPassword with email #input_email and password #input_password and not exiting user"(String input_email, String input_password) {
        when:
        AppUser appUser = appUserService.findByEmailAndPassword(input_email, input_password)

        then:
        appUser == null

        where:
        input_email   | input_password
        null          | null
        null          | ""
        null          | APPUSER_PASSWORD
        ""            | null
        ""            | ""
        ""            | APPUSER_PASSWORD
        APPUSER_EMAIL | null
        APPUSER_EMAIL | ""
        APPUSER_EMAIL | APPUSER_PASSWORD
    }

    def "findByEmailAndPassword with not exiting user but wrong email"() {
        given:
        appUserRepository.save(new AppUser("not_email", PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME))

        and:
        String email = "not_email"
        String password = APPUSER_PASSWORD

        when:
        AppUser appUser = appUserService.findByEmailAndPassword(email, password)

        then:
        appUser == null
    }

    def "findByEmailAndPassword with not exiting user but wrong password"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME))

        and:
        String email = APPUSER_EMAIL
        String password = "wrong_password"

        when:
        AppUser appUser = appUserService.findByEmailAndPassword(email, password)

        then:
        appUser == null
    }

    @Unroll
    def "findByEmailAndPassword with email #input_email and password #input_password and exiting user"(String input_email, String input_password) {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME))

        when:
        AppUser appUser = appUserService.findByEmailAndPassword(input_email, input_password)

        then:
        appUser == null

        where:
        input_email   | input_password
        APPUSER_EMAIL | null
        APPUSER_EMAIL | ""
    }

    @Unroll
    def "hasAllRoles with no roles in user: #roles | #result"(roles, result) {
        given:
        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
        appUser = appUserRepository.save(appUser)

        expect:
        appUserService.hasAllRoles(appUser, roles as Role[]) == result

        where:
        roles                                                                 | result
        null                                                                  | false
        []                                                                    | true
        [Role.APPUSER_VIEW]                                                   | false
        [Role.APPUSER_ADMIN]                                                  | false
        [Role.APPUSER_CHANGE_PASSWORD]                                        | false
        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN]                               | false
        [Role.APPUSER_VIEW, Role.APPUSER_CHANGE_PASSWORD]                     | false
        [Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD]                    | false
        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD] | false
    }

    @Unroll
    def "hasAllRoles with one role in user: #roles | #result"(roles, result) {
        given:
        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
        appUser.appUserRoles.add(Role.APPUSER_VIEW)
        appUser = appUserRepository.save(appUser)

        expect:
        appUserService.hasAllRoles(appUser, roles as Role[]) == result

        where:
        roles                                                                 | result
        null                                                                  | false
        []                                                                    | false
        [Role.APPUSER_VIEW]                                                   | true
        [Role.APPUSER_ADMIN]                                                  | false
        [Role.APPUSER_CHANGE_PASSWORD]                                        | false
        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN]                               | false
        [Role.APPUSER_VIEW, Role.APPUSER_CHANGE_PASSWORD]                     | false
        [Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD]                    | false
        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD] | false
    }

    @Unroll
    def "hasAllRoles with two roles in user: #roles | #result"(roles, result) {
        given:
        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
        appUser.appUserRoles.add(Role.APPUSER_VIEW)
        appUser.appUserRoles.add(Role.APPUSER_ADMIN)
        appUser = appUserRepository.save(appUser)

        expect:
        appUserService.hasAllRoles(appUser, roles as Role[]) == result

        where:
        roles                                                                 | result
        null                                                                  | false
        []                                                                    | false
        [Role.APPUSER_VIEW]                                                   | true
        [Role.APPUSER_ADMIN]                                                  | true
        [Role.APPUSER_CHANGE_PASSWORD]                                        | false
        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN]                               | true
        [Role.APPUSER_VIEW, Role.APPUSER_CHANGE_PASSWORD]                     | false
        [Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD]                    | false
        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD] | false
    }

    @Unroll
    def "hasAnyRoles with no roles in user: #roles | #result"(roles, result) {
        given:
        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
        appUser = appUserRepository.save(appUser)

        expect:
        appUserService.hasAnyRoles(appUser, roles as Role[]) == result

        where:
        roles                                                                 | result
        null                                                                  | false
        []                                                                    | true
        [Role.APPUSER_VIEW]                                                   | false
        [Role.APPUSER_ADMIN]                                                  | false
        [Role.APPUSER_CHANGE_PASSWORD]                                        | false
        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN]                               | false
        [Role.APPUSER_VIEW, Role.APPUSER_CHANGE_PASSWORD]                     | false
        [Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD]                    | false
        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD] | false
    }

    @Unroll
    def "hasAnyRoles with one role in user: #roles | #result"(roles, result) {
        given:
        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
        appUser.appUserRoles.add(Role.APPUSER_VIEW)
        appUser = appUserRepository.save(appUser)

        expect:
        appUserService.hasAnyRoles(appUser, roles as Role[]) == result

        where:
        roles                                                                 | result
        null                                                                  | false
        []                                                                    | false
        [Role.APPUSER_VIEW]                                                   | true
        [Role.APPUSER_ADMIN]                                                  | false
        [Role.APPUSER_CHANGE_PASSWORD]                                        | false
        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN]                               | true
        [Role.APPUSER_VIEW, Role.APPUSER_CHANGE_PASSWORD]                     | true
        [Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD]                    | false
        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD] | true
    }

    @Unroll
    def "hasAnyRoles with two roles in user: #roles | #result"(roles, result) {
        given:
        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
        appUser.appUserRoles.add(Role.APPUSER_VIEW)
        appUser.appUserRoles.add(Role.APPUSER_ADMIN)
        appUser = appUserRepository.save(appUser)

        expect:
        appUserService.hasAnyRoles(appUser, roles as Role[]) == result

        where:
        roles                                                                 | result
        null                                                                  | false
        []                                                                    | false
        [Role.APPUSER_VIEW]                                                   | true
        [Role.APPUSER_ADMIN]                                                  | true
        [Role.APPUSER_CHANGE_PASSWORD]                                        | false
        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN]                               | true
        [Role.APPUSER_VIEW, Role.APPUSER_CHANGE_PASSWORD]                     | true
        [Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD]                    | true
        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD] | true
    }

    def "newToken"() {
        given:
        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
        appUser = appUserRepository.save(appUser)

        when:
        int count = appUserTokenRepository.findByAppUser(appUser).size()

        then:
        count == 0

        when:
        appUserService.newToken(appUser)

        and:
        count = appUserTokenRepository.findByAppUser(appUser).size()

        then:
        count == 1
    }

    def "changePassword with null appUser and null password"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD)))

        when:
        boolean result = appUserService.changePassword(null, null)

        then:
        !result
    }

    def "changePassword with null appUser and empty password"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD)))

        when:
        boolean result = appUserService.changePassword(null, "")

        then:
        !result
    }

    def "changePassword with null appUser and valid password"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD)))

        when:
        boolean result = appUserService.changePassword(null, APPUSER_PASSWORD_NEW)

        then:
        !result
    }

    def "changePassword with valid appUser and null password"() {
        given:
        AppUser appUser = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD)))

        when:
        boolean result = appUserService.changePassword(appUser, null)

        then:
        !result
    }

    def "changePassword with valid appUser and empty password"() {
        given:
        AppUser appUser = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD)))

        when:
        boolean result = appUserService.changePassword(appUser, "")

        then:
        !result
    }

    def "changePassword with valid appUser and same password"() {
        given:
        AppUser appUser = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD)))

        when:
        appUserService.changePassword(appUser, APPUSER_PASSWORD)

        and:
        appUser = appUserService.findByEmailAndPassword(APPUSER_EMAIL, APPUSER_PASSWORD)

        then:
        appUser != null
        appUser.email == APPUSER_EMAIL
    }

    def "changePassword with valid appUser and valid password"() {
        given:
        AppUser appUser = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD)))

        when:
        appUserService.changePassword(appUser, APPUSER_PASSWORD_NEW)

        and:
        appUser = appUserService.findByEmailAndPassword(APPUSER_EMAIL, APPUSER_PASSWORD_NEW)

        then:
        appUser != null
        appUser.email == APPUSER_EMAIL
    }
}
