package org.thehellnet.lanparty.manager.service

import org.springframework.beans.factory.annotation.Autowired
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException
import org.thehellnet.lanparty.manager.model.persistence.Cfg
import org.thehellnet.lanparty.manager.model.persistence.Game
import org.thehellnet.lanparty.manager.model.persistence.Player
import spock.lang.Unroll

class CfgServiceTest extends ServiceSpecification {

    @Autowired
    private CfgService cfgService

    private Player player
    private Game game

    def setup() {
        player = createPlayer()
        game = gameRepository.findByTag(GAME_TAG)
    }

    @Unroll
    def "create valid user with \"#cfgContent\""(String cfgContent) {
        when:
        Cfg cfg = cfgService.create(player, game, cfgContent)

        then:
        cfg != null
        cfg.id != null
        cfg.player.id == player.id
        cfg.game.id == game.id
        cfg.cfg == cfgContent

        where:
        cfgContent << [null, "", CFG]
    }

    @Unroll
    def "create with null player, null game and \"#cfgContent\""(String cfgContent) {
        when:
        cfgService.create(null, null, cfgContent)

        then:
        thrown InvalidDataException

        where:
        cfgContent << [null, "", CFG]
    }

    @Unroll
    def "create with valid player, null game and \"#cfgContent\""(String cfgContent) {
        when:
        cfgService.create(player, null, cfgContent)

        then:
        thrown InvalidDataException

        where:
        cfgContent << [null, "", CFG]
    }

    @Unroll
    def "create with null player, valid game and \"#cfgContent\""(String cfgContent) {
        when:
        cfgService.create(null, game, cfgContent)

        then:
        thrown InvalidDataException

        where:
        cfgContent << [null, "", CFG]
    }

    def "get with existing id"() {
        given:
        Long cfgId = cfgRepository.save(new Cfg(player, game, CFG)).id

        when:
        Cfg cfg = cfgService.get(cfgId)

        then:
        cfg != null
        cfg.id == cfgId
        cfg.player == player
        cfg.game == game
        cfg.cfg == CFG
    }

    def "get with not existing id"() {
        given:
        Long cfgId = 12345678

        when:
        cfgService.get(cfgId)

        then:
        thrown NotFoundException
    }

    def "getAll with no cfg"() {
        when:
        List<Cfg> cfgs = cfgService.getAll()

        then:
        cfgs.size() == 0
    }

    def "getAll with one cfg"() {
        given:
        Long cfgId = cfgRepository.save(new Cfg(player, game, CFG)).id

        when:
        List<Cfg> cfgs = cfgService.getAll()

        then:
        cfgs.size() == 1

        when:
        Cfg cfg = cfgs.get(0)

        then:
        cfg != null
        cfg.id == cfgId
        cfg.player == player
        cfg.game == game
        cfg.cfg == CFG
    }

//    @Unroll
//    def "update UnchangedException #name name, #password password and #appUserRoles appUserRoles"(String name, String password, String[] appUserRoles, String barcode) {
//        given:
//        Long appUserId = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME, APPUSER_BARCODE)).id
//
//        when:
//        cfgService.update(appUserId, name, password, appUserRoles, barcode)
//
//        then:
//        thrown UnchangedException
//
//        where:
//        name | password | appUserRoles | barcode
//        null | null     | null         | null
//        null | ""       | null         | null
//    }
//
//    @Unroll
//    def "update with \"#name\" name, \"#password\" password, \"#appUserRoles\" appUserRolesand \"#barcode\" barcode"(String name, String password, String[] appUserRoles, String barcode) {
//        given:
//        Long appUserId = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME, APPUSER_BARCODE)).id
//
//        when:
//        cfgService.update(appUserId, name, password, appUserRoles, barcode)
//
//        and:
//        AppUser appUser = appUserRepository.getOne(appUserId)
//
//        then:
//        appUser != null
//        appUser.id == appUserId
//        appUser.email == APPUSER_EMAIL
//        PasswordUtility.verify(appUser.password, ((password != null && password.length() > 0) ? password : APPUSER_PASSWORD))
//        appUser.name == (name != null ? name : APPUSER_NAME)
//        appUser.barcode == (barcode != null ? barcode : APPUSER_BARCODE)
//        appUser.appUserRoles.size() == (appUserRoles != null ? appUserRoles.length : 0)
//
//        where:
//        name             | password             | appUserRoles                  | barcode
//        null             | null                 | [] as String[]                | null
//        null             | null                 | [] as String[]                | ""
//        null             | null                 | [] as String[]                | APPUSER_BARCODE_NEW
//        null             | null                 | [Role.LOGIN.name] as String[] | null
//        null             | null                 | [Role.LOGIN.name] as String[] | ""
//        null             | null                 | [Role.LOGIN.name] as String[] | APPUSER_BARCODE_NEW
//        null             | ""                   | [] as String[]                | null
//        null             | ""                   | [] as String[]                | ""
//        null             | ""                   | [] as String[]                | APPUSER_BARCODE_NEW
//        null             | ""                   | [Role.LOGIN.name] as String[] | null
//        null             | ""                   | [Role.LOGIN.name] as String[] | ""
//        null             | ""                   | [Role.LOGIN.name] as String[] | APPUSER_BARCODE_NEW
//        null             | APPUSER_PASSWORD_NEW | null                          | null
//        null             | APPUSER_PASSWORD_NEW | null                          | ""
//        null             | APPUSER_PASSWORD_NEW | null                          | APPUSER_BARCODE_NEW
//        null             | APPUSER_PASSWORD_NEW | [] as String[]                | null
//        null             | APPUSER_PASSWORD_NEW | [] as String[]                | ""
//        null             | APPUSER_PASSWORD_NEW | [] as String[]                | APPUSER_BARCODE_NEW
//        null             | APPUSER_PASSWORD_NEW | [Role.LOGIN.name] as String[] | null
//        null             | APPUSER_PASSWORD_NEW | [Role.LOGIN.name] as String[] | ""
//        null             | APPUSER_PASSWORD_NEW | [Role.LOGIN.name] as String[] | APPUSER_BARCODE_NEW
//        ""               | null                 | [] as String[]                | null
//        ""               | null                 | [] as String[]                | ""
//        ""               | null                 | [] as String[]                | APPUSER_BARCODE_NEW
//        ""               | null                 | [Role.LOGIN.name] as String[] | null
//        ""               | null                 | [Role.LOGIN.name] as String[] | ""
//        ""               | null                 | [Role.LOGIN.name] as String[] | APPUSER_BARCODE_NEW
//        ""               | ""                   | [] as String[]                | null
//        ""               | ""                   | [] as String[]                | ""
//        ""               | ""                   | [] as String[]                | APPUSER_BARCODE_NEW
//        ""               | ""                   | [Role.LOGIN.name] as String[] | null
//        ""               | ""                   | [Role.LOGIN.name] as String[] | ""
//        ""               | ""                   | [Role.LOGIN.name] as String[] | APPUSER_BARCODE_NEW
//        ""               | APPUSER_PASSWORD_NEW | null                          | null
//        ""               | APPUSER_PASSWORD_NEW | null                          | ""
//        ""               | APPUSER_PASSWORD_NEW | null                          | APPUSER_BARCODE_NEW
//        ""               | APPUSER_PASSWORD_NEW | [] as String[]                | null
//        ""               | APPUSER_PASSWORD_NEW | [] as String[]                | ""
//        ""               | APPUSER_PASSWORD_NEW | [] as String[]                | APPUSER_BARCODE_NEW
//        ""               | APPUSER_PASSWORD_NEW | [Role.LOGIN.name] as String[] | null
//        ""               | APPUSER_PASSWORD_NEW | [Role.LOGIN.name] as String[] | ""
//        ""               | APPUSER_PASSWORD_NEW | [Role.LOGIN.name] as String[] | APPUSER_BARCODE_NEW
//        APPUSER_NAME_NEW | null                 | null                          | null
//        APPUSER_NAME_NEW | null                 | null                          | ""
//        APPUSER_NAME_NEW | null                 | null                          | APPUSER_BARCODE_NEW
//        APPUSER_NAME_NEW | null                 | [] as String[]                | null
//        APPUSER_NAME_NEW | null                 | [] as String[]                | ""
//        APPUSER_NAME_NEW | null                 | [] as String[]                | APPUSER_BARCODE_NEW
//        APPUSER_NAME_NEW | null                 | [Role.LOGIN.name] as String[] | null
//        APPUSER_NAME_NEW | null                 | [Role.LOGIN.name] as String[] | ""
//        APPUSER_NAME_NEW | null                 | [Role.LOGIN.name] as String[] | APPUSER_BARCODE_NEW
//        APPUSER_NAME_NEW | ""                   | null                          | null
//        APPUSER_NAME_NEW | ""                   | null                          | ""
//        APPUSER_NAME_NEW | ""                   | null                          | APPUSER_BARCODE_NEW
//        APPUSER_NAME_NEW | ""                   | [] as String[]                | null
//        APPUSER_NAME_NEW | ""                   | [] as String[]                | ""
//        APPUSER_NAME_NEW | ""                   | [] as String[]                | APPUSER_BARCODE_NEW
//        APPUSER_NAME_NEW | ""                   | [Role.LOGIN.name] as String[] | null
//        APPUSER_NAME_NEW | ""                   | [Role.LOGIN.name] as String[] | ""
//        APPUSER_NAME_NEW | ""                   | [Role.LOGIN.name] as String[] | APPUSER_BARCODE_NEW
//        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | null                          | null
//        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | null                          | ""
//        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | null                          | APPUSER_BARCODE_NEW
//        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | [] as String[]                | null
//        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | [] as String[]                | ""
//        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | [] as String[]                | APPUSER_BARCODE_NEW
//        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | [Role.LOGIN.name] as String[] | null
//        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | [Role.LOGIN.name] as String[] | ""
//        APPUSER_NAME_NEW | APPUSER_PASSWORD_NEW | [Role.LOGIN.name] as String[] | APPUSER_BARCODE_NEW
//    }
//
//    def "update with invalid id"() {
//        given:
//        Long appUserId = 12345678
//
//        when:
//        cfgService.update(appUserId, APPUSER_NAME_NEW, APPUSER_PASSWORD_NEW, [Role.LOGIN.name] as String[], APPUSER_BARCODE_NEW)
//
//        then:
//        thrown NotFoundException
//    }
//
//    def "delete"() {
//        given:
//        Long appUserId = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD))).id
//
//        when:
//        cfgService.delete(appUserId)
//
//        then:
//        noExceptionThrown()
//
//        and:
//        appUserRepository.findAll().size() == 1
//    }
//
//    def "delete with not existing ID"() {
//        given:
//        Long appUserId = 12345678
//
//        when:
//        cfgService.delete(appUserId)
//
//        then:
//        thrown NotFoundException
//    }
//
//    def "findByEmail with admin"() {
//        given:
//        String email = "admin"
//
//        when:
//        AppUser appUser = cfgService.findByEmail(email)
//
//        then:
//        appUser != null
//        appUser.email == "admin"
//    }
//
//    def "findByEmail with exiting email"() {
//        given:
//        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME))
//
//        when:
//        AppUser appUser = cfgService.findByEmail(APPUSER_EMAIL)
//
//        then:
//        appUser != null
//        appUser.email == APPUSER_EMAIL
//    }
//
//    def "findByEmail with not exiting email"() {
//        given:
//        String email = APPUSER_EMAIL
//
//        when:
//        cfgService.findByEmail(email)
//
//        then:
//        thrown NotFoundException
//    }
//
//    def "findByEmail with not valid email"() {
//        given:
//        String email = "not_valid_email"
//
//        when:
//        cfgService.findByEmail(email)
//
//        then:
//        thrown NotFoundException
//    }
//
//    def "findByEmailAndPassword with admin user"() {
//        given:
//        String email = "admin"
//        String password = "admin"
//
//        when:
//        AppUser appUser = cfgService.findByEmailAndPassword(email, password)
//
//        then:
//        appUser != null
//        appUser.email == "admin"
//    }
//
//    def "findByEmailAndPassword with exiting user"() {
//        given:
//        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME))
//
//        when:
//        AppUser appUser = cfgService.findByEmailAndPassword(APPUSER_EMAIL, APPUSER_PASSWORD)
//
//        then:
//        appUser != null
//        appUser.email == APPUSER_EMAIL
//        appUser.name == APPUSER_NAME
//    }
//
//    def "findByEmailAndPassword with not exiting user"() {
//        given:
//        String email = APPUSER_EMAIL
//        String password = APPUSER_PASSWORD
//
//        when:
//        cfgService.findByEmailAndPassword(email, password)
//
//        then:
//        thrown NotFoundException
//    }
//
//    @Unroll
//    def "findByEmailAndPassword with email #input_email and password #input_password and not exiting user"(String input_email, String input_password) {
//        when:
//        cfgService.findByEmailAndPassword(input_email, input_password)
//
//        then:
//        thrown NotFoundException
//
//        where:
//        input_email   | input_password
//        null          | null
//        null          | ""
//        null          | APPUSER_PASSWORD
//        ""            | null
//        ""            | ""
//        ""            | APPUSER_PASSWORD
//        APPUSER_EMAIL | null
//        APPUSER_EMAIL | ""
//        APPUSER_EMAIL | APPUSER_PASSWORD
//    }
//
//    def "findByEmailAndPassword with not exiting user but wrong email"() {
//        given:
//        appUserRepository.save(new AppUser("not_email", PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME))
//
//        and:
//        String email = "not_email"
//        String password = APPUSER_PASSWORD
//
//        when:
//        cfgService.findByEmailAndPassword(email, password)
//
//        then:
//        thrown NotFoundException
//    }
//
//    def "findByEmailAndPassword with not exiting user but wrong password"() {
//        given:
//        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME))
//
//        and:
//        String email = APPUSER_EMAIL
//        String password = "wrong_password"
//
//        when:
//        cfgService.findByEmailAndPassword(email, password)
//
//        then:
//        thrown NotFoundException
//    }
//
//    @Unroll
//    def "findByEmailAndPassword with email #input_email and password #input_password and exiting user"(String input_email, String input_password) {
//        given:
//        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME))
//
//        when:
//        cfgService.findByEmailAndPassword(input_email, input_password)
//
//        then:
//        thrown NotFoundException
//
//        where:
//        input_email   | input_password
//        APPUSER_EMAIL | null
//        APPUSER_EMAIL | ""
//    }
//
//    def "findByBarcode with not registered barcode"() {
//        when:
//        cfgService.findByBarcode(APPUSER_BARCODE)
//
//        then:
//        thrown NotFoundException
//    }
//
//    def "findByBarcode with registered barcode"() {
//        given:
//        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME, APPUSER_BARCODE))
//
//        when:
//        AppUser appUser = cfgService.findByBarcode(APPUSER_BARCODE)
//
//        then:
//        appUser != null
//        appUser.email == APPUSER_EMAIL
//        appUser.name == APPUSER_NAME
//        appUser.barcode == APPUSER_BARCODE
//    }
//
//    def "findByBarcode with user and wrong barcode"() {
//        given:
//        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME, APPUSER_BARCODE))
//
//        when:
//        cfgService.findByBarcode(APPUSER_BARCODE_NEW)
//
//        then:
//        thrown NotFoundException
//    }
//
//    @Unroll
//    def "hasAllRoles with no roles in user: #roles | #result"(roles, result) {
//        given:
//        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
//        appUser = appUserRepository.save(appUser)
//
//        expect:
//        cfgService.hasAllRoles(appUser, roles as Role[]) == result
//
//        where:
//        roles                                                                 | result
//        null                                                                  | false
//        []                                                                    | true
//        [Role.APPUSER_VIEW]                                                   | false
//        [Role.APPUSER_ADMIN]                                                  | false
//        [Role.APPUSER_CHANGE_PASSWORD]                                        | false
//        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN]                               | false
//        [Role.APPUSER_VIEW, Role.APPUSER_CHANGE_PASSWORD]                     | false
//        [Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD]                    | false
//        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD] | false
//    }
//
//    @Unroll
//    def "hasAllRoles with one role in user: #roles | #result"(roles, result) {
//        given:
//        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
//        appUser.appUserRoles.add(Role.APPUSER_VIEW)
//        appUser = appUserRepository.save(appUser)
//
//        expect:
//        cfgService.hasAllRoles(appUser, roles as Role[]) == result
//
//        where:
//        roles                                                                 | result
//        null                                                                  | false
//        []                                                                    | false
//        [Role.APPUSER_VIEW]                                                   | true
//        [Role.APPUSER_ADMIN]                                                  | false
//        [Role.APPUSER_CHANGE_PASSWORD]                                        | false
//        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN]                               | false
//        [Role.APPUSER_VIEW, Role.APPUSER_CHANGE_PASSWORD]                     | false
//        [Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD]                    | false
//        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD] | false
//    }
//
//    @Unroll
//    def "hasAllRoles with two roles in user: #roles | #result"(roles, result) {
//        given:
//        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
//        appUser.appUserRoles.add(Role.APPUSER_VIEW)
//        appUser.appUserRoles.add(Role.APPUSER_ADMIN)
//        appUser = appUserRepository.save(appUser)
//
//        expect:
//        cfgService.hasAllRoles(appUser, roles as Role[]) == result
//
//        where:
//        roles                                                                 | result
//        null                                                                  | false
//        []                                                                    | false
//        [Role.APPUSER_VIEW]                                                   | true
//        [Role.APPUSER_ADMIN]                                                  | true
//        [Role.APPUSER_CHANGE_PASSWORD]                                        | false
//        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN]                               | true
//        [Role.APPUSER_VIEW, Role.APPUSER_CHANGE_PASSWORD]                     | false
//        [Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD]                    | false
//        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD] | false
//    }
//
//    @Unroll
//    def "hasAnyRoles with no roles in user: #roles | #result"(roles, result) {
//        given:
//        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
//        appUser = appUserRepository.save(appUser)
//
//        expect:
//        cfgService.hasAnyRoles(appUser, roles as Role[]) == result
//
//        where:
//        roles                                                                 | result
//        null                                                                  | false
//        []                                                                    | true
//        [Role.APPUSER_VIEW]                                                   | false
//        [Role.APPUSER_ADMIN]                                                  | false
//        [Role.APPUSER_CHANGE_PASSWORD]                                        | false
//        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN]                               | false
//        [Role.APPUSER_VIEW, Role.APPUSER_CHANGE_PASSWORD]                     | false
//        [Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD]                    | false
//        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD] | false
//    }
//
//    @Unroll
//    def "hasAnyRoles with one role in user: #roles | #result"(roles, result) {
//        given:
//        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
//        appUser.appUserRoles.add(Role.APPUSER_VIEW)
//        appUser = appUserRepository.save(appUser)
//
//        expect:
//        cfgService.hasAnyRoles(appUser, roles as Role[]) == result
//
//        where:
//        roles                                                                 | result
//        null                                                                  | false
//        []                                                                    | false
//        [Role.APPUSER_VIEW]                                                   | true
//        [Role.APPUSER_ADMIN]                                                  | false
//        [Role.APPUSER_CHANGE_PASSWORD]                                        | false
//        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN]                               | true
//        [Role.APPUSER_VIEW, Role.APPUSER_CHANGE_PASSWORD]                     | true
//        [Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD]                    | false
//        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD] | true
//    }
//
//    @Unroll
//    def "hasAnyRoles with two roles in user: #roles | #result"(roles, result) {
//        given:
//        AppUser appUser = new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME)
//        appUser.appUserRoles.add(Role.APPUSER_VIEW)
//        appUser.appUserRoles.add(Role.APPUSER_ADMIN)
//        appUser = appUserRepository.save(appUser)
//
//        expect:
//        cfgService.hasAnyRoles(appUser, roles as Role[]) == result
//
//        where:
//        roles                                                                 | result
//        null                                                                  | false
//        []                                                                    | false
//        [Role.APPUSER_VIEW]                                                   | true
//        [Role.APPUSER_ADMIN]                                                  | true
//        [Role.APPUSER_CHANGE_PASSWORD]                                        | false
//        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN]                               | true
//        [Role.APPUSER_VIEW, Role.APPUSER_CHANGE_PASSWORD]                     | true
//        [Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD]                    | true
//        [Role.APPUSER_VIEW, Role.APPUSER_ADMIN, Role.APPUSER_CHANGE_PASSWORD] | true
//    }
//
//    def "newToken with valid user"() {
//        when:
//        AppUser appUser = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME))
//
//        then:
//        appUserTokenRepository.findByAppUser(appUser).size() == 0
//
//        when:
//        cfgService.newToken(appUser)
//
//        then:
//        appUserTokenRepository.findByAppUser(appUser).size() == 1
//    }
//
//    def "newToken with invalid user"() {
//        when:
//        cfgService.newToken(null)
//
//        then:
//        thrown InvalidDataException
//    }
}
