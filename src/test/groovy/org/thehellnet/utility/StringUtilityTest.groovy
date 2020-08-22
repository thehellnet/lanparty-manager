package org.thehellnet.utility

import spock.lang.Specification
import spock.lang.Unroll

class StringUtilityTest extends Specification {

    @Unroll
    def "splitLines \"#input\" \"#expected\""(String input, List<String> expected) {
        when:
        List<String> actual = StringUtility.splitLines(input)

        then:
        actual == expected

        where:
        input                | expected
        null                 | []
        ""                   | []
        " "                  | []
        "  "                 | []
        "\n"                 | []
        " \n"                | []
        "\n "                | []
        " \n "               | []
        "\n\n"               | []
        "\n \n"              | []
        " \n \n"             | []
        " \n \n "            | []
        " \n  \n "           | []
        "\n\r"               | []
        "\n \r"              | []
        " \n \r"             | []
        " \n \r "            | []
        " \n  \r "           | []
        "test"               | ["test"]
        " test"              | ["test"]
        "test "              | ["test"]
        " test "             | ["test"]
        "test test"          | ["test test"]
        "test test "         | ["test test"]
        " test test"         | ["test test"]
        " test test "        | ["test test"]
        " test    test "     | ["test    test"]
        " test   \n test "   | ["test", "test"]
        " test   \n test\n " | ["test", "test"]
        "test\nTEST\ntest"   | ["test", "TEST", "test"]
        " test   \n test\r " | ["test", "test"]
        "test\nTEST\rtest"   | ["test", "TEST", "test"]
    }

    @Unroll
    def "joinLines \"#input\" \"#expected\""(List<String> input, String expected) {
        when:
        String actual = StringUtility.joinLines(input)

        then:
        actual == expected

        where:
        input                    | expected
        null                     | ""
        []                       | ""
        ["test"]                 | "test"
        ["test test"]            | "test test"
        ["test", "test"]         | "test\ntest"
        ["test", " test"]        | "test\ntest"
        ["test ", "test"]        | "test\ntest"
        ["test ", " test"]       | "test\ntest"
        ["test ", " test "]      | "test\ntest"
        [" test ", " test "]     | "test\ntest"
        ["test", "TEST", "test"] | "test\nTEST\ntest"
    }

    @Unroll
    def "splitSpaces \"#input\" \"#expected\""(String input, List<String> expected) {
        when:
        List<String> actual = StringUtility.splitSpaces(input)

        then:
        actual == expected

        where:
        input                | expected
        null                 | null
        ""                   | []
        " "                  | []
        "  "                 | []
        "\n"                 | []
        " \n"                | []
        "\n "                | []
        " \n "               | []
        "\n\n"               | []
        "\n \n"              | []
        " \n \n"             | []
        " \n \n "            | []
        " \n  \n "           | []
        "\n\r"               | []
        "\n \r"              | []
        " \n \r"             | []
        " \n \r "            | []
        " \n  \r "           | []
        "test"               | ["test"]
        " test"              | ["test"]
        "test "              | ["test"]
        " test "             | ["test"]
        "test test"          | ["test", "test"]
        "test test "         | ["test", "test"]
        " test test"         | ["test", "test"]
        " test test "        | ["test", "test"]
        " test    test "     | ["test", "test"]
        " test   \n test "   | ["test", "test"]
        " test   \n test\n " | ["test", "test"]
        "test\nTEST\ntest"   | ["testTESTtest"]
        " test   \n test\r " | ["test", "test"]
        "test\nTEST\rtest"   | ["testTESTtest"]
    }

    @Unroll
    def "joinSpaces \"#input\" \"#expected\""(List<String> input, String expected) {
        when:
        String actual = StringUtility.joinSpaces(input)

        then:
        actual == expected

        where:
        input                    | expected
        null                     | ""
        []                       | ""
        ["test"]                 | "test"
        ["test test"]            | "test test"
        ["test", "test"]         | "test test"
        ["test", " test"]        | "test test"
        ["test ", "test"]        | "test test"
        ["test ", " test"]       | "test test"
        ["test ", " test "]      | "test test"
        [" test ", " test "]     | "test test"
        ["test", "TEST", "test"] | "test TEST test"
    }

    @Unroll
    def "firstLetterLowercase \"#input\" \"#expected\""(String input, String expected) {
        when:
        String actual = StringUtility.firstLetterLowercase(input)

        then:
        actual == expected

        where:
        input  | expected
        null   | null
        ""     | ""
        "test" | "test"
        "Test" | "test"
        "TEST" | "tEST"
        "tEST" | "tEST"
        "TeSt" | "teSt"
    }
}
