package org.thehellnet.utility

import spock.lang.Specification

class ResourceUtilityTest extends Specification {

    def "getResource"() {
        given:
        String path = "configuration/persistence.yml"

        when:
        InputStream inputStream = ResourceUtility.getResource(path)

        then:
        inputStream != null
    }
}
