package org.thehellnet.lanparty.manager.service

import org.springframework.beans.factory.annotation.Autowired
import org.thehellnet.lanparty.manager.model.persistence.AppUser
import org.thymeleaf.context.Context

class TemplateServiceTest extends ServiceSpecification {

    @Autowired
    private TemplateService templateService

    def "render"() {
        given:
        String templateCategory = "mail"
        String templateName = "appuser-activation"

        AppUser appUser = new AppUser(
                email: APPUSER_EMAIL,
                name: APPUSER_NAME,
                nickname: APPUSER_NICKNAME
        )

        String link = "https://tournaments.thehellnet.org/confirm/1234abcd"

        Context context = new Context();
        context.setVariable("appUser", appUser);
        context.setVariable("link", link);

        when:
        String output = templateService.render(templateCategory, templateName, context)
        println output

        then:
        noExceptionThrown()
        output != null
        output.length() > 0
    }
}
