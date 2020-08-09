package org.thehellnet.utility

import spock.lang.Specification
import spock.lang.Unroll

class Q3AUtilsTest extends Specification {

    @Unroll
    def "removeColorCodes: \"#input\" - \"#expected\""(String input, String expected) {
        given:
        String actual = Q3AUtils.removeColorCodes(input)

        expect:
        actual == expected

        where:
        input                                   | expected
        "dm^7"                                  | "dm"
        "^7"                                    | ""
        "^7^7"                                  | ""
        " dm^7"                                 | "dm"
        "dm^7 "                                 | "dm"
        " dm^7 "                                | "dm"
        "^7dm^7"                                | "dm"
        "^6^7dm^7"                              | "dm"
        " ^6^7dm^7"                             | "dm"
        "^6^7dm^7 "                             | "dm"
        " ^6^7dm^7 "                            | "dm"
        "^6dm ^7dm"                             | "dm dm"
        " ^6dm ^7dm"                            | "dm dm"
        "^6dm ^7dm "                            | "dm dm"
        " ^6dm ^7dm "                           | "dm dm"
        "^4G^1o^3o^4g^2l^1e"                    | "Google"
        " ^4G^1o^3o^4g^2l^1e"                   | "Google"
        "^4G^1o^3o^4g^2l^1e "                   | "Google"
        " ^4G^7^1o^7^3o^7^4g^7^2l^7^1e^7 "      | "Google"
        " ^4G ^1o ^3o ^4g ^2l ^1e "             | "G o o g l e"
        " ^4G^7 ^1o^7 ^3o^7 ^4g^7 ^2l^7 ^1e^7 " | "G o o g l e"
    }

    @Unroll
    def "tagToColor: \"#input\" - \"#expected\""(String input, String expected) {
        given:
        String actual = Q3AUtils.tagToColor(input)

        expect:
        actual == expected

        where:
        input                          | expected
        "[hnt]\${cyan}theory"          | "[hnt]^5theory"
        "[hnt]\${cyan}theory\${white}" | "[hnt]^5theory^7"
    }

    @Unroll
    def "colorToTag: \"#input\" - \"#expected\""(String input, String expected) {
        given:
        String actual = Q3AUtils.colorToTag(input)

        expect:
        actual == expected

        where:
        input             | expected
        "[hnt]^5theory^7" | "[hnt]\${cyan}theory\${white}"
    }

    @Unroll
    def "clearString: \"#input\" - \"#expected\""(String input, String expected) {
        given:
        String actual = Q3AUtils.clearString(input)

        expect:
        actual == expected

        where:
        input             | expected
        "[hnt]^5theory^7" | "[hnt]^5theory"
    }

    @Unroll
    def "pingToInteger: \"#input\" - \"#expected\""(String input, String expected) {
        given:
        String actual = Q3AUtils.pingToInteger(input)

        expect:
        actual == expected

        where:
        input  | expected
        "0"    | 0
        "1"    | 1
        "10"   | 10
        "999"  | -1
        null   | -1
        "-1"   | -1
        "ZMBI" | -1
        ""     | -1
    }

    @Unroll
    def "cleanIpAddress: \"#input\" - \"#expected\""(String input, String expected) {
        given:
        String actual = Q3AUtils.cleanIpAddress(input)

        expect:
        actual == expected

        where:
        input                   | expected
        "1.2.3.4:12345"         | "1.2.3.4"
        "100.200.300.400:12345" | "100.200.300.400"
        "1.2.3.4:"              | "1.2.3.4"
        "1.2.3.4"               | "1.2.3.4"
    }
}
