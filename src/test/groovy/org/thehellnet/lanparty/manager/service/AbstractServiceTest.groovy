package org.thehellnet.lanparty.manager.service

import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException
import spock.lang.Specification

class AbstractServiceTest extends Specification {

    def "parseStringList with null input"() {
        given:
        Object input = null

        when:
        AbstractService.parseStringList(input)

        then:
        thrown InvalidDataException
    }

    def "parseStringList not required with null input"() {
        given:
        Object input = null

        when:
        String[] actual = AbstractService.parseStringList(input, false)

        then:
        actual.length == 0
    }

    def "parseStringList with string input"() {
        given:
        Object input = ""

        when:
        AbstractService.parseStringList(input)

        then:
        thrown InvalidDataException
    }

    def "parseStringList not required with string input"() {
        given:
        Object input = ""

        when:
        String[] actual = AbstractService.parseStringList(input, false)

        then:
        actual.length == 0
    }

    def "parseStringList with empty input"() {
        given:
        Object input = [] as List<String>

        when:
        String[] actual = AbstractService.parseStringList(input)

        then:
        actual != null
        actual.length == 0
    }

    def "parseStringList not required with empty input"() {
        given:
        Object input = [] as List<String>

        when:
        String[] actual = AbstractService.parseStringList(input, false)

        then:
        actual != null
        actual.length == 0
    }

    def "parseStringList with one input"() {
        given:
        Object input = ["string"] as List<String>

        when:
        String[] actual = AbstractService.parseStringList(input)

        then:
        actual != null
        actual.length == 1
    }

    def "parseStringList not required with one input"() {
        given:
        Object input = ["string"] as List<String>

        when:
        String[] actual = AbstractService.parseStringList(input, false)

        then:
        actual != null
        actual.length == 1
    }
}
