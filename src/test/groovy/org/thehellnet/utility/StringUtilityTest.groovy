package org.thehellnet.utility

import spock.lang.Specification

class StringUtilityTest extends Specification {

    def "splitLines"() {
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
                "\n\r"              : [],
                "\n \r"             : [],
                " \n \r"            : [],
                " \n \r "           : [],
                " \n  \r "          : [],
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
                "test\nTEST\ntest"  : ["test", "TEST", "test"],
                " test   \n test\r ": ["test", "test"],
                "test\nTEST\rtest"  : ["test", "TEST", "test"]
        ]

        expect:
        assert StringUtility.splitLines(null) == null

        DATA.each {
            if (StringUtility.splitLines(it.key) != it.value) {
                assert false
            }
        }
    }

    def "joinLines"() {
        given:
        def DATA = [
                []                      : "",
                ["test"]                : "test",
                ["test test"]           : "test test",
                ["test", "test"]        : "test\ntest",
                ["test", " test"]       : "test\ntest",
                ["test ", "test"]       : "test\ntest",
                ["test ", " test"]      : "test\ntest",
                ["test ", " test "]     : "test\ntest",
                [" test ", " test "]    : "test\ntest",
                ["test", "TEST", "test"]: "test\nTEST\ntest"
        ]

        expect:
        assert StringUtility.joinLines(null) == null

        DATA.each {
            if (StringUtility.joinLines(it.key) != it.value) {
                assert false
            }
        }
    }

    def "splitSpaces"() {
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
                "\n\r"              : [],
                "\n \r"             : [],
                " \n \r"            : [],
                " \n \r "           : [],
                " \n  \r "          : [],
                "test"              : ["test"],
                " test"             : ["test"],
                "test "             : ["test"],
                " test "            : ["test"],
                "test test"         : ["test", "test"],
                "test test "        : ["test", "test"],
                " test test"        : ["test", "test"],
                " test test "       : ["test", "test"],
                " test    test "    : ["test", "test"],
                " test   \n test "  : ["test", "test"],
                " test   \n test\n ": ["test", "test"],
                "test\nTEST\ntest"  : ["testTESTtest"],
                " test   \n test\r ": ["test", "test"],
                "test\nTEST\rtest"  : ["testTESTtest"]
        ]

        expect:
        assert StringUtility.splitSpaces(null) == null

        DATA.each {
            if (StringUtility.splitSpaces(it.key) != it.value) {
                assert false
            }
        }
    }

    def "joinSpaces"() {
        given:
        def DATA = [
                []                      : "",
                ["test"]                : "test",
                ["test test"]           : "test test",
                ["test", "test"]        : "test test",
                ["test", " test"]       : "test test",
                ["test ", "test"]       : "test test",
                ["test ", " test"]      : "test test",
                ["test ", " test "]     : "test test",
                [" test ", " test "]    : "test test",
                ["test", "TEST", "test"]: "test TEST test"
        ]

        expect:
        assert StringUtility.joinSpaces(null) == null

        DATA.each {
            if (StringUtility.joinSpaces(it.key) != it.value) {
                assert false
            }
        }
    }
}
