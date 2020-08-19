package org.thehellnet.lanparty.manager.service

import org.springframework.beans.factory.annotation.Autowired
import org.thehellnet.lanparty.manager.exception.InvalidDataException
import org.thehellnet.lanparty.manager.exception.NotFoundException
import org.thehellnet.lanparty.manager.model.dto.request.auth.ConfirmAuthRequestDTO
import org.thehellnet.lanparty.manager.model.dto.request.auth.LoginAuthRequestDTO
import org.thehellnet.lanparty.manager.model.dto.request.auth.RegisterAuthRequestDTO
import org.thehellnet.lanparty.manager.model.dto.response.auth.LoginAuthResponseDTO
import org.thehellnet.lanparty.manager.model.dto.response.auth.RegisterAuthResponseDTO
import org.thehellnet.lanparty.manager.model.persistence.AppUser
import org.thehellnet.utility.PasswordUtility
import spock.lang.Unroll

import java.time.LocalDate
import java.time.LocalDateTime

class AuthServiceTest extends ServiceSpecification {

    @Autowired
    private AuthService authService

    def "findByEmail with admin"() {
        given:
        String email = "admin"

        when:
        AppUser appUser = authService.findByEnabledTrueAndEmail(email)

        then:
        appUser != null
        appUser.email == "admin"
    }

    def "findByEmail with exiting email activated"() {
        given:
        AppUser user = new AppUser(APPUSER_EMAIL, PasswordUtility.newInstance().hash(APPUSER_PASSWORD), APPUSER_NAME)
        user.enabled = true
        appUserRepository.save(user)

        when:
        AppUser appUser = authService.findByEnabledTrueAndEmail(APPUSER_EMAIL)

        then:
        appUser != null
        appUser.email == APPUSER_EMAIL
    }

    def "findByEmail with exiting email not activated"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.newInstance().hash(APPUSER_PASSWORD), APPUSER_NAME))

        when:
        authService.findByEnabledTrueAndEmail(APPUSER_EMAIL)

        then:
        thrown NotFoundException
    }

    def "findByEmail with not exiting email"() {
        given:
        String email = APPUSER_EMAIL

        when:
        authService.findByEnabledTrueAndEmail(email)

        then:
        thrown NotFoundException
    }

    def "findByEmail with not valid email"() {
        given:
        String email = "not_valid_email"

        when:
        authService.findByEnabledTrueAndEmail(email)

        then:
        thrown NotFoundException
    }

    def "findByEmailAndPassword with exiting user"() {
        given:
        AppUser user = new AppUser(APPUSER_EMAIL, PasswordUtility.newInstance().hash(APPUSER_PASSWORD), APPUSER_NAME)
        user.enabled = true
        appUserRepository.save(user)

        when:
        AppUser appUser = authService.findByEnabledTrueAndEmailAndPassword(APPUSER_EMAIL, APPUSER_PASSWORD)

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
        AppUser appUser = authService.findByEnabledTrueAndEmailAndPassword(email, password)

        then:
        appUser != null
        appUser.email == "admin"
    }

    def "findByEmailAndPassword with not exiting user"() {
        given:
        String email = APPUSER_EMAIL
        String password = APPUSER_PASSWORD

        when:
        authService.findByEnabledTrueAndEmailAndPassword(email, password)

        then:
        thrown NotFoundException
    }

    @Unroll
    def "findByEmailAndPassword with email #input_email and password #input_password and not exiting user"(String input_email, String input_password) {
        when:
        authService.findByEnabledTrueAndEmailAndPassword(input_email, input_password)

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
        appUserRepository.save(new AppUser("not_email", PasswordUtility.newInstance().hash(APPUSER_PASSWORD), APPUSER_NAME))

        and:
        String email = "not_email"
        String password = APPUSER_PASSWORD

        when:
        authService.findByEnabledTrueAndEmailAndPassword(email, password)

        then:
        thrown NotFoundException
    }

    def "findByEmailAndPassword with not exiting user but wrong password"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.newInstance().hash(APPUSER_PASSWORD), APPUSER_NAME))

        and:
        String email = APPUSER_EMAIL
        String password = "wrong_password"

        when:
        authService.findByEnabledTrueAndEmailAndPassword(email, password)

        then:
        thrown NotFoundException
    }

    @Unroll
    def "findByEmailAndPassword with email #input_email and password #input_password and exiting user"(String input_email, String input_password) {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.newInstance().hash(APPUSER_PASSWORD), APPUSER_NAME))

        when:
        authService.findByEnabledTrueAndEmailAndPassword(input_email, input_password)

        then:
        thrown NotFoundException

        where:
        input_email   | input_password
        APPUSER_EMAIL | null
        APPUSER_EMAIL | ""
    }

    def "newToken with valid user"() {
        when:
        AppUser appUser = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.newInstance().hash(APPUSER_PASSWORD), APPUSER_NAME))

        then:
        appUserTokenRepository.findByAppUser(appUser).size() == 0

        when:
        authService.newToken(appUser)

        then:
        appUserTokenRepository.findByAppUser(appUser).size() == 1
    }

    def "newToken with invalid user"() {
        when:
        authService.newToken(null)

        then:
        thrown InvalidDataException
    }

    def "login with invalid user"() {
        given:
        LoginAuthRequestDTO requestDTO = new LoginAuthRequestDTO()
        requestDTO.email = "invalid"
        requestDTO.password = "notvalid"

        when:
        LoginAuthResponseDTO responseDTO = authService.login(requestDTO)

        then:
        thrown NotFoundException
    }

    def "login with admin user"() {
        given:
        LoginAuthRequestDTO requestDTO = new LoginAuthRequestDTO()
        requestDTO.email = "admin"
        requestDTO.password = "admin"

        when:
        LoginAuthResponseDTO responseDTO = authService.login(requestDTO)

        then:
        noExceptionThrown()
        responseDTO.expiration.isAfter(LocalDateTime.now())
    }

    def "login with new created user"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.newInstance().hash(APPUSER_PASSWORD)))

        when:
        LoginAuthRequestDTO requestDTO = new LoginAuthRequestDTO()
        requestDTO.email = APPUSER_EMAIL
        requestDTO.password = APPUSER_PASSWORD

        authService.login(requestDTO)

        then:
        thrown NotFoundException
    }

    def "login with new created user enabled"() {
        given:
        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.newInstance().hash(APPUSER_PASSWORD))
        appUser.enabled = true
        appUserRepository.save(appUser)

        when:
        LoginAuthRequestDTO requestDTO = new LoginAuthRequestDTO()
        requestDTO.email = APPUSER_EMAIL
        requestDTO.password = APPUSER_PASSWORD

        LoginAuthResponseDTO responseDTO = authService.login(requestDTO)

        then:
        noExceptionThrown()
        responseDTO.expiration.isAfter(LocalDateTime.now())
    }

    def "register new user"() {
        given:
        RegisterAuthRequestDTO requestDTO = new RegisterAuthRequestDTO();
        requestDTO.email = APPUSER_EMAIL
        requestDTO.password = APPUSER_PASSWORD
        requestDTO.name = APPUSER_NAME
        requestDTO.nickname = APPUSER_NICKNAME

        when:
        RegisterAuthResponseDTO responseDTO = authService.register(requestDTO)

        then:
        noExceptionThrown()
        responseDTO.email == APPUSER_EMAIL
        responseDTO.name == APPUSER_NAME
        responseDTO.nickname == APPUSER_NICKNAME

        and:
        appUserRepository.findAll().size() == 2

        when:
        AppUser appUser = appUserRepository.findByEmail(APPUSER_EMAIL)

        then:
        !appUser.enabled
    }

    def "register and confirm new user"() {
        given:
        RegisterAuthRequestDTO requestDTO = new RegisterAuthRequestDTO();
        requestDTO.email = APPUSER_EMAIL
        requestDTO.password = APPUSER_PASSWORD
        requestDTO.name = APPUSER_NAME
        requestDTO.nickname = APPUSER_NICKNAME

        when:
        RegisterAuthResponseDTO responseDTO = authService.register(requestDTO)

        then:
        noExceptionThrown()
        responseDTO.email == APPUSER_EMAIL
        responseDTO.name == APPUSER_NAME
        responseDTO.nickname == APPUSER_NICKNAME

        and:
        appUserRepository.findAll().size() == 2

        when:
        AppUser appUser = appUserRepository.findByEmail(APPUSER_EMAIL)

        then:
        !appUser.enabled

        when:
        ConfirmAuthRequestDTO confirmAuthRequestDTO = new ConfirmAuthRequestDTO();
        confirmAuthRequestDTO.email = APPUSER_EMAIL
        confirmAuthRequestDTO.confirmCode = appUser.confirmCode

        and:
        authService.confirm(confirmAuthRequestDTO)

        then:
        noExceptionThrown()

        when:
        appUser = appUserRepository.findByEmail(APPUSER_EMAIL)

        then:
        appUser.enabled
        appUser.confirmCode == null
        appUser.confirmTs != null && appUser.confirmTs.isBefore(LocalDateTime.now())
    }
}
