package org.thehellnet.lanparty.manager.service

import org.springframework.beans.factory.annotation.Autowired

class MailServiceTest extends ServiceSpecification {

    @Autowired
    private MailService mailService

    def "send bMail"() {
        given:
        String to = ""
        String subject = "Test"
        String body = "Ciao"

        when:
        mailService.sendMail(to, subject, body)

        then:
        noExceptionThrown()
    }
}
