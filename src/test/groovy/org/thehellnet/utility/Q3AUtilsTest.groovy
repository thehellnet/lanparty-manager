package org.thehellnet.utility

import spock.lang.Specification

class Q3AUtilsTest extends Specification {

    private String input
    private String expected
    private String actual
    private int pingExpected
    private int pingActual

    def "removeColorCodes"() {
        given:
        def VALUES = [
                "dm^7"                                 : "dm",
                "^7"                                   : "",
                "^7^7"                                 : "",
                " dm^7"                                : "dm",
                "dm^7 "                                : "dm",
                " dm^7 "                               : "dm",
                "^7dm^7"                               : "dm",
                "^6^7dm^7"                             : "dm",
                " ^6^7dm^7"                            : "dm",
                "^6^7dm^7 "                            : "dm",
                " ^6^7dm^7 "                           : "dm",
                "^6dm ^7dm"                            : "dm dm",
                " ^6dm ^7dm"                           : "dm dm",
                "^6dm ^7dm "                           : "dm dm",
                " ^6dm ^7dm "                          : "dm dm",
                "^4G^1o^3o^4g^2l^1e"                   : "Google",
                " ^4G^1o^3o^4g^2l^1e"                  : "Google",
                "^4G^1o^3o^4g^2l^1e "                  : "Google",
                " ^4G^7^1o^7^3o^7^4g^7^2l^7^1e^7 "     : "Google",
                " ^4G ^1o ^3o ^4g ^2l ^1e "            : "G o o g l e",
                " ^4G^7 ^1o^7 ^3o^7 ^4g^7 ^2l^7 ^1e^7 ": "G o o g l e"
        ]

        expect:
        VALUES.each {
            assert Q3AUtils.removeColorCodes(it.key) == it.value
        }
    }

    def "tagToColor"() throws Exception {
        given:
        def VALUES = [
                "[hnt]\${cyan}theory"         : "[hnt]^5theory",
                "[hnt]\${cyan}theory\${white}": "[hnt]^5theory^7",
                "[hnt]\${cyan}theory\${/cyan}": "[hnt]^5theory\${/cyan}",
        ]

        expect:
        VALUES.each {
            assert Q3AUtils.tagToColor(it.key) == it.value
        }
    }

    def "colorToTag"() throws Exception {
        given:
        def VALUES = [
                "[hnt]^5theory^7": "[hnt]\${cyan}theory\${white}",
                "[hnt]^5theory^9": "[hnt]\${cyan}theory^9"
        ]

        expect:
        VALUES.each {
            assert Q3AUtils.colorToTag(it.key) == it.value
        }
    }

    def "clearString"() throws Exception {
        given:
        def VALUES = [
                "[hnt]^5theory^7": "[hnt]^5theory"
        ]

        expect:
        VALUES.each {
            assert Q3AUtils.clearString(it.key) == it.value
        }
    }

    def "pingToInteger"() throws Exception {
        given:
        def VALUES = [
                "0"   : 0,
                "1"   : 1,
                "10"  : 10,
                "999" : -1,
                null  : -1,
                "-1"  : -1,
                "ZMBI": -1,
                ""    : -1
        ]

        expect:
        VALUES.each {
            assert Q3AUtils.pingToInteger(it.key) == it.value
        }
    }

    def "cleanIpAddress"() throws Exception {
        given:
        def VALUES = [
                "1.2.3.4:12345"        : "1.2.3.4",
                "100.200.300.400:12345": "100.200.300.400",
                "1.2.3.4:"             : "1.2.3.4",
                "1.2.3.4"              : "1.2.3.4"
        ]

        expect:
        VALUES.each {
            assert Q3AUtils.cleanIpAddress(it.key) == it.value
        }
    }
}
