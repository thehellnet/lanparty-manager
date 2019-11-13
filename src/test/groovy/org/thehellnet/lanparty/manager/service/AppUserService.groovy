package org.thehellnet.lanparty.manager.service

import org.springframework.beans.factory.annotation.Autowired
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException
import org.thehellnet.lanparty.manager.model.constant.Role
import org.thehellnet.lanparty.manager.model.persistence.AppUser
import org.thehellnet.utility.PasswordUtility

class AppUserServiceSpecification extends ServiceSpecification {

    @Autowired
    private AppUserService appUserService

    def "findByBarcode with not registered barcode"() {
        when:
        appUserService.findByBarcode(APPUSER_BARCODE)

        then:
        thrown NotFoundException
    }

    def "findByBarcode with registered barcode"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME, [] as Set<Role>, APPUSER_BARCODE))

        when:
        AppUser appUser = appUserService.findByBarcode(APPUSER_BARCODE)

        then:
        appUser != null
        appUser.email == APPUSER_EMAIL
        appUser.name == APPUSER_NAME
        appUser.appUserRoles.size() == 0
        appUser.barcode == APPUSER_BARCODE
    }

    def "findByBarcode with user and wrong barcode"() {
        given:
        appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD), APPUSER_NAME, [] as Set<Role>, APPUSER_BARCODE))

        when:
        appUserService.findByBarcode(APPUSER_BARCODE_NEW)

        then:
        thrown NotFoundException
    }
}
