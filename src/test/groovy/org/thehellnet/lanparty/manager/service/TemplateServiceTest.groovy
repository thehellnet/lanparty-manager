package org.thehellnet.lanparty.manager.service

import org.springframework.beans.factory.annotation.Autowired
import org.thymeleaf.context.Context

class TemplateServiceTest extends ServiceSpecification {

    @Autowired
    private TemplateService templateService

    def "render"() {
        given:
        String templateCategory = "mail"
        String templateName = "appuser-activation"

        Context context = new Context();
        context.setVariable("name", "World");

        when:
        String output = templateService.render(templateCategory, templateName, context)
        println output

        then:
        noExceptionThrown()
        output != null
        output.length() > 0
    }
}
