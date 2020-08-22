package org.thehellnet.utility

import spock.lang.Specification

class ResourceUtilityTest extends Specification {

    def "getResource"() {
        given:
        String input = "configuration/persistence.yml"

        when:
        InputStream inputStream = ResourceUtility.getResource(input)

        then:
        inputStream != null
    }

    def "getResource internal only"() {
        given:
        String input = "configuration/persistence.yml"

        when:
        InputStream inputStream = ResourceUtility.getResource(input, true)

        then:
        inputStream != null
    }

    def "getResourceContent"() {
        given:
        String input = "configuration/persistence.yml"

        when:
        String actual = ResourceUtility.getResourceContent(input)

        then:
        actual != null
        actual.length() > 0
    }

    def "getResourceContent internal only"() {
        given:
        String input = "configuration/persistence.yml"

        when:
        String actual = ResourceUtility.getResourceContent(input)

        then:
        actual != null
        actual.length() > 0
    }
}
