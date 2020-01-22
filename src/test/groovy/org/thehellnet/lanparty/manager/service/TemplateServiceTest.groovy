package org.thehellnet.lanparty.manager.service

import org.springframework.beans.factory.annotation.Autowired
import org.thehellnet.lanparty.manager.model.persistence.AppUser
import org.thehellnet.lanparty.manager.model.template.AppUserRegistrationConfirmTemplate

class TemplateServiceTest extends ServiceSpecification {

    @Autowired
    private TemplateService templateService

    def "render"() {
        given:
        AppUserRegistrationConfirmTemplate template = new AppUserRegistrationConfirmTemplate(
                new AppUser(
                        email: APPUSER_EMAIL,
                        name: APPUSER_NAME,
                        nickname: APPUSER_NICKNAME
                ),
                "https://tournaments.thehellnet.org/confirm/1234abcd"
        )

        when:
        String output = templateService.render(template)
        println output

        then:
        noExceptionThrown()
        output != null
        output.length() > 0
    }
}
