package org.thehellnet.lanparty.manager.service

import org.springframework.beans.factory.annotation.Autowired

class MailServiceTest extends ServiceSpecification {

    @Autowired
    private MailService mailService

//    def "send"() {
//        given:
//        String to = ""
//        String subject = "Test"
//        String body = "Ciao"
//
//        when:
//        mailService.send(to, subject, body)
//
//        then:
//        noExceptionThrown()
//    }
//
    def "sendHtml"() {
        given:
        String to = ""
        String subject = "Test"
        String body = "<h1>Test</h1><br /><p>Prova</p>"

        when:
        mailService.sendHtml(to, subject, body)

        then:
        noExceptionThrown()
    }
}
