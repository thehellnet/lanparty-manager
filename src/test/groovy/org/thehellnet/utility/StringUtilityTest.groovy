package org.thehellnet.utility

import spock.lang.Specification

class StringUtilityTest extends Specification {

    def "SplitLines"() {
        given:
        def DATA = [
                ""                  : [],
                " "                 : [],
                "  "                : [],
                "\n"                : [],
                " \n"               : [],
                "\n "               : [],
                " \n "              : [],
                "\n\n"              : [],
                "\n \n"             : [],
                " \n \n"            : [],
                " \n \n "           : [],
                " \n  \n "          : [],
                "test"              : ["test"],
                " test"             : ["test"],
                "test "             : ["test"],
                " test "            : ["test"],
                "test test"         : ["test test"],
                "test test "        : ["test test"],
                " test test"        : ["test test"],
                " test test "       : ["test test"],
                " test    test "    : ["test    test"],
                " test   \n test "  : ["test", "test"],
                " test   \n test\n ": ["test", "test"],
                "test\nTEST\ntest"  : ["test", "TEST", "test"]
        ]

        expect:
        assert StringUtility.splitLines(null) == null

        DATA.each {
            if (StringUtility.splitLines(it.key) != it.value) {
                assert false
            }
        }
    }
}
