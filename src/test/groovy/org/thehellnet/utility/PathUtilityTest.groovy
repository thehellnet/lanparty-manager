package org.thehellnet.utility

import spock.lang.Specification
import spock.lang.Unroll

class PathUtilityTest extends Specification {

    @Unroll
    def "join: \"#input\" -> \"#expected\""(String[] input, String expected) {
        when:
        String actual = PathUtility.join(input)

        then:
        actual == expected

        where:
        input                    | expected
        null                     | ""

        []                       | ""
        [""]                     | ""
        [" "]                    | ""
        ["/"]                    | "/"
        ["//"]                   | "/"

        ["test"]                 | "test"
        ["test/"]                | "test"
        ["test//"]               | "test"
        ["/test"]                | "/test"
        ["/test/"]               | "/test"
        ["/test//"]              | "/test"
        ["//test"]               | "/test"
        ["//test/"]              | "/test"
        ["//test//"]             | "/test"

        ["", "test"]             | "/test"
        ["", "test/"]            | "/test"
        ["", "test//"]           | "/test"
        ["", "/test"]            | "/test"
        ["", "/test/"]           | "/test"
        ["", "/test//"]          | "/test"
        ["", "//test"]           | "/test"
        ["", "//test/"]          | "/test"
        ["", "//test//"]         | "/test"

        [" ", "test"]            | "/test"
        [" ", "test/"]           | "/test"
        [" ", "test//"]          | "/test"
        [" ", "/test"]           | "/test"
        [" ", "/test/"]          | "/test"
        [" ", "/test//"]         | "/test"
        [" ", "//test"]          | "/test"
        [" ", "//test/"]         | "/test"
        [" ", "//test//"]        | "/test"

        ["/", "test"]            | "/test"
        ["/", "test/"]           | "/test"
        ["/", "test//"]          | "/test"
        ["/", "/test"]           | "/test"
        ["/", "/test/"]          | "/test"
        ["/", "/test//"]         | "/test"
        ["/", "//test"]          | "/test"
        ["/", "//test/"]         | "/test"
        ["/", "//test//"]        | "/test"

        ["//", "test"]           | "/test"
        ["//", "test/"]          | "/test"
        ["//", "test//"]         | "/test"
        ["//", "/test"]          | "/test"
        ["//", "/test/"]         | "/test"
        ["//", "/test//"]        | "/test"
        ["//", "//test"]         | "/test"
        ["//", "//test/"]        | "/test"
        ["//", "//test//"]       | "/test"

        ["test", "test"]         | "test/test"
        ["test", "test/"]        | "test/test"
        ["test", "test//"]       | "test/test"
        ["test", "/test"]        | "test/test"
        ["test", "/test/"]       | "test/test"
        ["test", "/test//"]      | "test/test"
        ["test", "//test"]       | "test/test"
        ["test", "//test/"]      | "test/test"
        ["test", "//test//"]     | "test/test"

        [" test", "test"]        | "test/test"
        [" test", "test/"]       | "test/test"
        [" test", "test//"]      | "test/test"
        [" test", "/test"]       | "test/test"
        [" test", "/test/"]      | "test/test"
        [" test", "/test//"]     | "test/test"
        [" test", "//test"]      | "test/test"
        [" test", "//test/"]     | "test/test"
        [" test", "//test//"]    | "test/test"

        ["test/", "test"]        | "test/test"
        ["test/", "test/"]       | "test/test"
        ["test/", "test//"]      | "test/test"
        ["test/", "/test"]       | "test/test"
        ["test/", "/test/"]      | "test/test"
        ["test/", "/test//"]     | "test/test"
        ["test/", "//test"]      | "test/test"
        ["test/", "//test/"]     | "test/test"
        ["test/", "//test//"]    | "test/test"

        ["test//", "test"]       | "test/test"
        ["test//", "test/"]      | "test/test"
        ["test//", "test//"]     | "test/test"
        ["test//", "/test"]      | "test/test"
        ["test//", "/test/"]     | "test/test"
        ["test//", "/test//"]    | "test/test"
        ["test//", "//test"]     | "test/test"
        ["test//", "//test/"]    | "test/test"
        ["test//", "//test//"]   | "test/test"

        ["/test", "test"]        | "/test/test"
        ["/test", "test/"]       | "/test/test"
        ["/test", "test//"]      | "/test/test"
        ["/test", "/test"]       | "/test/test"
        ["/test", "/test/"]      | "/test/test"
        ["/test", "/test//"]     | "/test/test"
        ["/test", "//test"]      | "/test/test"
        ["/test", "//test/"]     | "/test/test"
        ["/test", "//test//"]    | "/test/test"

        [" /test", "test"]       | "/test/test"
        [" /test", "test/"]      | "/test/test"
        [" /test", "test//"]     | "/test/test"
        [" /test", "/test"]      | "/test/test"
        [" /test", "/test/"]     | "/test/test"
        [" /test", "/test//"]    | "/test/test"
        [" /test", "//test"]     | "/test/test"
        [" /test", "//test/"]    | "/test/test"
        [" /test", "//test//"]   | "/test/test"

        ["/test/", "test"]       | "/test/test"
        ["/test/", "test/"]      | "/test/test"
        ["/test/", "test//"]     | "/test/test"
        ["/test/", "/test"]      | "/test/test"
        ["/test/", "/test/"]     | "/test/test"
        ["/test/", "/test//"]    | "/test/test"
        ["/test/", "//test"]     | "/test/test"
        ["/test/", "//test/"]    | "/test/test"
        ["/test/", "//test//"]   | "/test/test"

        ["/test//", "test"]      | "/test/test"
        ["/test//", "test/"]     | "/test/test"
        ["/test//", "test//"]    | "/test/test"
        ["/test//", "/test"]     | "/test/test"
        ["/test//", "/test/"]    | "/test/test"
        ["/test//", "/test//"]   | "/test/test"
        ["/test//", "//test"]    | "/test/test"
        ["/test//", "//test/"]   | "/test/test"
        ["/test//", "//test//"]  | "/test/test"

        ["//test", "test"]       | "/test/test"
        ["//test", "test/"]      | "/test/test"
        ["//test", "test//"]     | "/test/test"
        ["//test", "/test"]      | "/test/test"
        ["//test", "/test/"]     | "/test/test"
        ["//test", "/test//"]    | "/test/test"
        ["//test", "//test"]     | "/test/test"
        ["//test", "//test/"]    | "/test/test"
        ["//test", "//test//"]   | "/test/test"

        [" //test", "test"]      | "/test/test"
        [" //test", "test/"]     | "/test/test"
        [" //test", "test//"]    | "/test/test"
        [" //test", "/test"]     | "/test/test"
        [" //test", "/test/"]    | "/test/test"
        [" //test", "/test//"]   | "/test/test"
        [" //test", "//test"]    | "/test/test"
        [" //test", "//test/"]   | "/test/test"
        [" //test", "//test//"]  | "/test/test"

        ["//test/", "test"]      | "/test/test"
        ["//test/", "test/"]     | "/test/test"
        ["//test/", "test//"]    | "/test/test"
        ["//test/", "/test"]     | "/test/test"
        ["//test/", "/test/"]    | "/test/test"
        ["//test/", "/test//"]   | "/test/test"
        ["//test/", "//test"]    | "/test/test"
        ["//test/", "//test/"]   | "/test/test"
        ["//test/", "//test//"]  | "/test/test"

        ["//test//", "test"]     | "/test/test"
        ["//test//", "test/"]    | "/test/test"
        ["//test//", "test//"]   | "/test/test"
        ["//test//", "/test"]    | "/test/test"
        ["//test//", "/test/"]   | "/test/test"
        ["//test//", "/test//"]  | "/test/test"
        ["//test//", "//test"]   | "/test/test"
        ["//test//", "//test/"]  | "/test/test"
        ["//test//", "//test//"] | "/test/test"
    }
}
