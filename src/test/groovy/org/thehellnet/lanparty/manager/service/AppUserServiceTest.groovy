package org.thehellnet.lanparty.manager.service

import org.springframework.beans.factory.annotation.Autowired
import org.thehellnet.lanparty.manager.ContextSpecification
import org.thehellnet.lanparty.manager.exception.appuser.AppUserAlreadyPresentException
import org.thehellnet.lanparty.manager.exception.appuser.AppUserInvalidMailException
import org.thehellnet.lanparty.manager.exception.appuser.AppUserNotFoundException
import org.thehellnet.lanparty.manager.model.constant.Role
import org.thehellnet.lanparty.manager.model.persistence.AppUser
import org.thehellnet.lanparty.manager.repository.AppUserRepository

class AppUserServiceTest extends ContextSpecification {

    private static final String APPUSER_EMAIL = "user@domain.tdl"
    private static final String APPUSER_NAME = "User"
    private static final String APPUSER_PASSWORD = "password"

    private static final String APPUSER_NAME_NEW = "New name"

    @Autowired
    private AppUserRepository appUserRepository

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
        appUserRepository.save(new AppUser(APPUSER_EMAIL, APPUSER_PASSWORD))

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
        Long userId = appUserRepository.save(new AppUser(APPUSER_EMAIL, APPUSER_PASSWORD)).id

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
        appUser.email == APPUSER_EMAIL
        appUser.name == APPUSER_NAME
        appUser.appUserRoles.size() == 0
    }

    def "create normal user with null name"() {
        when:
        AppUser appUser = appUserService.create(APPUSER_EMAIL, APPUSER_PASSWORD, null)

        then:
        appUser != null
        appUser.email == APPUSER_EMAIL
        appUser.name == null
        appUser.appUserRoles.size() == 0
    }

    def "create normal user with empty name"() {
        when:
        AppUser appUser = appUserService.create(APPUSER_EMAIL, APPUSER_PASSWORD, "")

        then:
        appUser != null
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
        AppUser createdAppUser = new AppUser(APPUSER_EMAIL, APPUSER_PASSWORD, APPUSER_NAME)
        createdAppUser = appUserRepository.save(createdAppUser)
        Long appUserId = createdAppUser.id

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
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_EMAIL, APPUSER_PASSWORD, APPUSER_NAME)).id

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
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_EMAIL, APPUSER_PASSWORD, APPUSER_NAME)).id

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
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_EMAIL, APPUSER_PASSWORD, APPUSER_NAME)).id

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
        Long appUserId = appUserRepository.save(new AppUser(APPUSER_EMAIL, APPUSER_PASSWORD, APPUSER_NAME)).id

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

    def "Delete"() {
    }

    def "FindByEmail"() {
    }

    def "FindByEmailAndPassword"() {
    }

    def "NewToken"() {
    }

    def "HasAllRoles"() {
    }

    def "HasAnyRoles"() {
    }

    def "ChangePassword"() {
    }
}
