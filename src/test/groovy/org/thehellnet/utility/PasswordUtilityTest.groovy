package org.thehellnet.utility

import org.thehellnet.utility.exception.InvalidValueException
import spock.lang.Specification
import spock.lang.Unroll

class PasswordUtilityTest extends Specification {

    private PasswordUtility passwordUtility

    @SuppressWarnings("unused")
    def setup() {
        passwordUtility = PasswordUtility.newInstance()
    }

    def "hash with null input"() {
        given:
        String input = null

        when:
        passwordUtility.hash(input)

        then:
        thrown InvalidValueException
    }

    def "hash with empty input"() {
        given:
        String input = ""

        when:
        passwordUtility.hash(input)

        then:
        thrown InvalidValueException
    }

    def "hash with valid input"() {
        given:
        String input = "test"

        when:
        String actual = passwordUtility.hash(input)

        then:
        noExceptionThrown()
        actual != null
        actual.length() > 0
    }

    @Unroll
    def "verify with hash \"#inputHash\" and password \"#inputPassword\""(String inputHash, String inputPassword) {
        when:
        passwordUtility.verify(inputHash, inputPassword)

        then:
        thrown InvalidValueException

        where:
        inputHash | inputPassword
        null      | null
        null      | ""
        ""        | null
        ""        | ""
    }

    def "verify with valid input value"() {
        given:
        String password = "test"

        when:
        String hashedPassword = passwordUtility.hash(password)

        then:
        noExceptionThrown()
        hashedPassword != null
        hashedPassword.length() > 0

        when:
        boolean actual = passwordUtility.verify(hashedPassword, password)

        then:
        noExceptionThrown()
        actual
    }
}
