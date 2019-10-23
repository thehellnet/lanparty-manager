package org.thehellnet.lanparty.manager.service

import org.springframework.beans.factory.annotation.Autowired
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException
import org.thehellnet.lanparty.manager.exception.controller.UnauthorizedException
import org.thehellnet.lanparty.manager.model.constant.Role
import org.thehellnet.lanparty.manager.model.persistence.AppUser
import org.thehellnet.utility.PasswordUtility
import spock.lang.Unroll

class LoginServiceTest extends ServiceSpecification {

    @Autowired
    private LoginService loginService

    def "findByEmail with admin"() {
        given:
        String email = "admin"

        when:
        AppUser appUser = loginService.findByEmail(email)

        then:
        appUser != null
        appUser.email == "admin"
    }

    def "findByEmail with exiting email"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME))

        when:
        AppUser appUser = loginService.findByEmail(APPUSER_EMAIL)

        then:
        appUser != null
        appUser.email == APPUSER_EMAIL
    }

    def "findByEmail with not exiting email"() {
        given:
        String email = APPUSER_EMAIL

        when:
        loginService.findByEmail(email)

        then:
        thrown NotFoundException
    }

    def "findByEmail with not valid email"() {
        given:
        String email = "not_valid_email"

        when:
        loginService.findByEmail(email)

        then:
        thrown NotFoundException
    }

    def "findByEmailAndPassword with exiting user but without LOGIN role"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME))

        and:
        String email = APPUSER_EMAIL
        String password = APPUSER_PASSWORD

        when:
        loginService.findByEmailAndPassword(email, password)

        then:
        thrown UnauthorizedException
    }

    def "findByEmailAndPassword with exiting user"() {
        given:
        AppUser user = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
        user.getAppUserRoles().add(Role.ACTION_LOGIN)
        appUserRepository.save(user)

        when:
        AppUser appUser = loginService.findByEmailAndPassword(APPUSER_EMAIL, APPUSER_PASSWORD)

        then:
        appUser != null
        appUser.email == APPUSER_EMAIL
        appUser.name == APPUSER_NAME
    }

    def "findByEmailAndPassword with admin user"() {
        given:
        String email = "admin"
        String password = "admin"

        when:
        AppUser appUser = loginService.findByEmailAndPassword(email, password)

        then:
        appUser != null
        appUser.email == "admin"
    }

    def "findByEmailAndPassword with not exiting user"() {
        given:
        String email = APPUSER_EMAIL
        String password = APPUSER_PASSWORD

        when:
        loginService.findByEmailAndPassword(email, password)

        then:
        thrown NotFoundException
    }

    @Unroll
    def "findByEmailAndPassword with email #input_email and password #input_password and not exiting user"(String input_email, String input_password) {
        when:
        loginService.findByEmailAndPassword(input_email, input_password)

        then:
        thrown NotFoundException

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
        loginService.findByEmailAndPassword(email, password)

        then:
        thrown NotFoundException
    }

    def "findByEmailAndPassword with not exiting user but wrong password"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME))

        and:
        String email = APPUSER_EMAIL
        String password = "wrong_password"

        when:
        loginService.findByEmailAndPassword(email, password)

        then:
        thrown NotFoundException
    }

    @Unroll
    def "findByEmailAndPassword with email #input_email and password #input_password and exiting user"(String input_email, String input_password) {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME))

        when:
        loginService.findByEmailAndPassword(input_email, input_password)

        then:
        thrown NotFoundException

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
        loginService.hasAllRoles(appUser, roles as Role[]) == result

        where:
        roles                                                                              | result
        null                                                                               | false
        []                                                                                 | true
        [Role.APPUSER_CREATE]                                                              | false
        [Role.APPUSER_READ]                                                                | false
        [Role.APPUSER_UPDATE]                                                              | false
        [Role.APPUSER_DELETE]                                                              | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ]                                           | false
        [Role.APPUSER_CREATE, Role.APPUSER_UPDATE]                                         | false
        [Role.APPUSER_CREATE, Role.APPUSER_DELETE]                                         | false
        [Role.APPUSER_READ, Role.APPUSER_UPDATE]                                           | false
        [Role.APPUSER_READ, Role.APPUSER_DELETE]                                           | false
        [Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                                         | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_UPDATE]                      | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_DELETE]                      | false
        [Role.APPUSER_CREATE, Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                    | false
        [Role.APPUSER_READ, Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                      | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_UPDATE, Role.APPUSER_DELETE] | false
    }

    @Unroll
    def "hasAllRoles with one role in user: #roles | #result"(roles, result) {
        given:
        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
        appUser.appUserRoles.add(Role.APPUSER_READ)
        appUser = appUserRepository.save(appUser)

        expect:
        loginService.hasAllRoles(appUser, roles as Role[]) == result

        where:
        roles                                                                              | result
        null                                                                               | false
        []                                                                                 | false
        [Role.APPUSER_CREATE]                                                              | false
        [Role.APPUSER_READ]                                                                | true
        [Role.APPUSER_UPDATE]                                                              | false
        [Role.APPUSER_DELETE]                                                              | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ]                                           | false
        [Role.APPUSER_CREATE, Role.APPUSER_UPDATE]                                         | false
        [Role.APPUSER_CREATE, Role.APPUSER_DELETE]                                         | false
        [Role.APPUSER_READ, Role.APPUSER_UPDATE]                                           | false
        [Role.APPUSER_READ, Role.APPUSER_DELETE]                                           | false
        [Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                                         | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_UPDATE]                      | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_DELETE]                      | false
        [Role.APPUSER_CREATE, Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                    | false
        [Role.APPUSER_READ, Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                      | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_UPDATE, Role.APPUSER_DELETE] | false
    }

    @Unroll
    def "hasAllRoles with two roles in user: #roles | #result"(roles, result) {
        given:
        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
        appUser.appUserRoles.add(Role.APPUSER_READ)
        appUser.appUserRoles.add(Role.APPUSER_UPDATE)
        appUser = appUserRepository.save(appUser)

        expect:
        loginService.hasAllRoles(appUser, roles as Role[]) == result

        where:
        roles                                                                              | result
        null                                                                               | false
        []                                                                                 | false
        [Role.APPUSER_CREATE]                                                              | false
        [Role.APPUSER_READ]                                                                | true
        [Role.APPUSER_UPDATE]                                                              | true
        [Role.APPUSER_DELETE]                                                              | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ]                                           | false
        [Role.APPUSER_CREATE, Role.APPUSER_UPDATE]                                         | false
        [Role.APPUSER_CREATE, Role.APPUSER_DELETE]                                         | false
        [Role.APPUSER_READ, Role.APPUSER_UPDATE]                                           | true
        [Role.APPUSER_READ, Role.APPUSER_DELETE]                                           | false
        [Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                                         | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_UPDATE]                      | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_DELETE]                      | false
        [Role.APPUSER_CREATE, Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                    | false
        [Role.APPUSER_READ, Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                      | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_UPDATE, Role.APPUSER_DELETE] | false
    }

    @Unroll
    def "hasAnyRoles with no roles in user: #roles | #result"(roles, result) {
        given:
        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
        appUser = appUserRepository.save(appUser)

        expect:
        loginService.hasAnyRoles(appUser, roles as Role[]) == result

        where:
        roles                                                                              | result
        null                                                                               | false
        []                                                                                 | true
        [Role.APPUSER_CREATE]                                                              | false
        [Role.APPUSER_READ]                                                                | false
        [Role.APPUSER_UPDATE]                                                              | false
        [Role.APPUSER_DELETE]                                                              | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ]                                           | false
        [Role.APPUSER_CREATE, Role.APPUSER_UPDATE]                                         | false
        [Role.APPUSER_CREATE, Role.APPUSER_DELETE]                                         | false
        [Role.APPUSER_READ, Role.APPUSER_UPDATE]                                           | false
        [Role.APPUSER_READ, Role.APPUSER_DELETE]                                           | false
        [Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                                         | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_UPDATE]                      | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_DELETE]                      | false
        [Role.APPUSER_CREATE, Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                    | false
        [Role.APPUSER_READ, Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                      | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_UPDATE, Role.APPUSER_DELETE] | false
    }

    @Unroll
    def "hasAnyRoles with one role in user: #roles | #result"(roles, result) {
        given:
        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
        appUser.appUserRoles.add(Role.APPUSER_READ)
        appUser = appUserRepository.save(appUser)

        expect:
        loginService.hasAnyRoles(appUser, roles as Role[]) == result

        where:
        roles                                                                              | result
        null                                                                               | false
        []                                                                                 | false
        [Role.APPUSER_CREATE]                                                              | false
        [Role.APPUSER_READ]                                                                | true
        [Role.APPUSER_UPDATE]                                                              | false
        [Role.APPUSER_DELETE]                                                              | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ]                                           | true
        [Role.APPUSER_CREATE, Role.APPUSER_UPDATE]                                         | false
        [Role.APPUSER_CREATE, Role.APPUSER_DELETE]                                         | false
        [Role.APPUSER_READ, Role.APPUSER_UPDATE]                                           | true
        [Role.APPUSER_READ, Role.APPUSER_DELETE]                                           | true
        [Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                                         | false
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_UPDATE]                      | true
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_DELETE]                      | true
        [Role.APPUSER_CREATE, Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                    | false
        [Role.APPUSER_READ, Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                      | true
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_UPDATE, Role.APPUSER_DELETE] | true
    }

    @Unroll
    def "hasAnyRoles with two roles in user: #roles | #result"(roles, result) {
        given:
        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
        appUser.appUserRoles.add(Role.APPUSER_READ)
        appUser.appUserRoles.add(Role.APPUSER_DELETE)
        appUser = appUserRepository.save(appUser)

        expect:
        loginService.hasAnyRoles(appUser, roles as Role[]) == result

        where:
        roles                                                                              | result
        null                                                                               | false
        []                                                                                 | false
        [Role.APPUSER_CREATE]                                                              | false
        [Role.APPUSER_READ]                                                                | true
        [Role.APPUSER_UPDATE]                                                              | false
        [Role.APPUSER_DELETE]                                                              | true
        [Role.APPUSER_CREATE, Role.APPUSER_READ]                                           | true
        [Role.APPUSER_CREATE, Role.APPUSER_UPDATE]                                         | false
        [Role.APPUSER_CREATE, Role.APPUSER_DELETE]                                         | true
        [Role.APPUSER_READ, Role.APPUSER_UPDATE]                                           | true
        [Role.APPUSER_READ, Role.APPUSER_DELETE]                                           | true
        [Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                                         | true
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_UPDATE]                      | true
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_DELETE]                      | true
        [Role.APPUSER_CREATE, Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                    | true
        [Role.APPUSER_READ, Role.APPUSER_UPDATE, Role.APPUSER_DELETE]                      | true
        [Role.APPUSER_CREATE, Role.APPUSER_READ, Role.APPUSER_UPDATE, Role.APPUSER_DELETE] | true
    }

    def "newToken with valid user"() {
        when:
        AppUser appUser = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME))

        then:
        appUserTokenRepository.findByAppUser(appUser).size() == 0

        when:
        loginService.newToken(appUser)

        then:
        appUserTokenRepository.findByAppUser(appUser).size() == 1
    }

    def "newToken with invalid user"() {
        when:
        loginService.newToken(null)

        then:
        thrown InvalidDataException
    }
}
