package org.thehellnet.utility

import spock.lang.Specification
import spock.lang.Unroll

class EmailUtilityTest extends Specification {

    @Unroll
    def "validate #expected with \"#input\""(String input, boolean expected) {
        given:
        boolean actual = EmailUtility.validate(input)

        expect:
        actual == expected

        where:
        input      | expected
        null       | false
        ""         | false
        " "        | false
        "  "       | false
        "d "       | false
        " d"       | false
        " d "      | false
        " @ "      | false
        " @ s"     | false
        " @s"      | false
        "s@s"      | false
        "s@s."     | false
        "s@s.i"    | false
        "s@s.i "   | false
        " s@s.i"   | false
        " s@s.i "  | false
        "s@s.it"   | true
        " s@s.it"  | true
        "s@s.it "  | true
        " s@s.it " | true
        "admin"    | false
        " admin"   | false
        "admin "   | false
        " admin "  | false
    }

    @Unroll
    def "validateForLogin #expected with \"#input\""(String input, boolean expected) {
        given:
        boolean actual = EmailUtility.validateForLogin(input)

        expect:
        actual == expected

        where:
        input      | expected
        null       | false
        ""         | false
        " "        | false
        "  "       | false
        "d "       | false
        " d"       | false
        " d "      | false
        " @ "      | false
        " @ s"     | false
        " @s"      | false
        "s@s"      | false
        "s@s."     | false
        "s@s.i"    | false
        "s@s.i "   | false
        " s@s.i"   | false
        " s@s.i "  | false
        "s@s.it"   | true
        " s@s.it"  | true
        "s@s.it "  | true
        " s@s.it " | true
        "admin"    | true
        " admin"   | true
        "admin "   | true
        " admin "  | true
    }

    @Unroll
    def "checkNullOrEmpty #expected with \"#input\""(String input, boolean expected) {
        given:
        boolean actual = EmailUtility.checkNullOrEmpty(input)

        expect:
        actual == expected

        where:
        input | expected
        null  | true
        ""    | true
        " "   | true
        "  "  | true
        "d "  | false
        " d"  | false
        " d " | false
    }
}
