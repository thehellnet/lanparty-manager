package org.thehellnet.lanparty.manager.utility.cfg

import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand
import spock.lang.Specification
import spock.lang.Unroll

class ParsedCfgCommandParserTest extends Specification {

    private ParsedCfgCommandUtility<String, List<ParsedCfgCommand>> utility = new ParsedCfgCommandParser()

    @Unroll
    def "parseCommand with \"#input\""(String input, ParsedCfgCommand expected) {
        when:
        ParsedCfgCommand actual = ParsedCfgCommandParser.parseCommand(input)

        then:
        actual == expected

        where:
        input                              | expected
        null                               | null
        ""                                 | null
        " "                                | null
        "  "                               | null
        "\n"                               | null
        " \n"                              | null
        "\n "                              | null
        " \n "                             | null
        "\n\n"                             | null
        "\n \n"                            | null
        " \n \n"                           | null
        " \n \n "                          | null
        " \n  \n "                         | null
        "\n\r"                             | null
        "\n \r"                            | null
        " \n \r"                           | null
        " \n \r "                          | null
        " \n  \r "                         | null
        "test"                             | new ParsedCfgCommand("test")
        " test"                            | new ParsedCfgCommand("test")
        "test "                            | new ParsedCfgCommand("test")
        " test "                           | new ParsedCfgCommand("test")
        "test test"                        | new ParsedCfgCommand("test", "test")
        "test test "                       | new ParsedCfgCommand("test", "test")
        " test test"                       | new ParsedCfgCommand("test", "test")
        " test test "                      | new ParsedCfgCommand("test", "test")
        " test    test "                   | new ParsedCfgCommand("test", "test")
        " test   \n test "                 | null
        " test   \n test\n "               | null
        "test\nTEST\ntest"                 | null
        " test   \n test\r "               | null
        "test\nTEST\rtest"                 | null
        "test TEST test"                   | new ParsedCfgCommand("test", "TEST", "test")
        "test   TEST test"                 | new ParsedCfgCommand("test", "TEST", "test")
        "test TEST   test"                 | new ParsedCfgCommand("test", "TEST", "test")
        "test   TEST   test"               | new ParsedCfgCommand("test", "TEST", "test")
        "test   TEST   test  "             | new ParsedCfgCommand("test", "TEST", "test")
        "  test   TEST   test  "           | new ParsedCfgCommand("test", "TEST", "test")
        "  test   TEST   test"             | new ParsedCfgCommand("test", "TEST", "test")
        "test TEST test tesssst"           | new ParsedCfgCommand("test", "TEST", "test tesssst")
        "test TEST test   tesssst"         | new ParsedCfgCommand("test", "TEST", "test tesssst")
        "test TEST test   tesssst "        | new ParsedCfgCommand("test", "TEST", "test tesssst")
        "test TEST   test     tesssst"     | new ParsedCfgCommand("test", "TEST", "test tesssst")
        "test   TEST   test tesssst"       | new ParsedCfgCommand("test", "TEST", "test tesssst")
        "  test   TEST   test tesssst er"  | new ParsedCfgCommand("test", "TEST", "test tesssst er")
        "  test   TEST   test tesssst  er" | new ParsedCfgCommand("test", "TEST", "test tesssst er")
        "test TEST \"test tesssst  er\""   | new ParsedCfgCommand("test", "TEST", "test tesssst er")
        "unbindall"                        | new ParsedCfgCommand("unbindall")
        "unbindall test"                   | new ParsedCfgCommand("unbindall")
        "unbindall test one"               | new ParsedCfgCommand("unbindall")
        "unbindall test one two"           | new ParsedCfgCommand("unbindall")
        "unbindall"                        | new ParsedCfgCommand("unbindall")
        "unbindall \"test\""               | new ParsedCfgCommand("unbindall")
        "unbindall \"test one\""           | new ParsedCfgCommand("unbindall")
        "unbindall \"test one two\""       | new ParsedCfgCommand("unbindall")
        "unbindall 'test'"                 | new ParsedCfgCommand("unbindall")
        "unbindall 'test one'"             | new ParsedCfgCommand("unbindall")
        "unbindall 'test one two'"         | new ParsedCfgCommand("unbindall")
        "say ciao"                         | new ParsedCfgCommand("say", null, "ciao")
        "say ciao ciao"                    | new ParsedCfgCommand("say", null, "ciao ciao")
        "say \'ciao ciao\'"                | new ParsedCfgCommand("say", null, "ciao ciao")
        "say \"ciao ciao\""                | new ParsedCfgCommand("say", null, "ciao ciao")
        "sensitivity 23"                   | new ParsedCfgCommand("sensitivity", null, "23")
        "one"                              | new ParsedCfgCommand("one")
        "one two"                          | new ParsedCfgCommand("one", "two")
        "one two three"                    | new ParsedCfgCommand("one", "two", "three")
        "one two  three"                   | new ParsedCfgCommand("one", "two", "three")
        "one two three "                   | new ParsedCfgCommand("one", "two", "three")
        "one two  three "                  | new ParsedCfgCommand("one", "two", "three")
        "one two \"three\""                | new ParsedCfgCommand("one", "two", "three")
        "one two \" three\""               | new ParsedCfgCommand("one", "two", " three")
        "one two \"three \""               | new ParsedCfgCommand("one", "two", "three ")
        "one two \" three \""              | new ParsedCfgCommand("one", "two", " three ")
        "one two three four"               | new ParsedCfgCommand("one", "two", "three four")
        "one two  three four"              | new ParsedCfgCommand("one", "two", "three four")
        "one two three  four"              | new ParsedCfgCommand("one", "two", "three four")
        "one two  three  four"             | new ParsedCfgCommand("one", "two", "three four")
        "one two \"three four\""           | new ParsedCfgCommand("one", "two", "three four")
    }

    def "parse with null input"() {
        given:
        String input = null
        List<ParsedCfgCommand> expected = []

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual == expected
    }

    def "parse with empty input"() {
        given:
        String input = ""
        List<ParsedCfgCommand> expected = []

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual == expected
    }

    def "parse with blank lines"() {
        given:
        String input = "\n\n\n"
        List<ParsedCfgCommand> expected = []

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual == expected
    }

    def "parse with single line"() {
        given:
        String input = "unbindall"
        List<ParsedCfgCommand> expected = [new ParsedCfgCommand("unbindall")]

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual == expected
    }

    def "parse with two lines"() {
        given:
        String input = "unbindall\n" +
                "say ciao ciao"

        List<ParsedCfgCommand> expected = [
                new ParsedCfgCommand("unbindall"),
                new ParsedCfgCommand("say", null, "ciao ciao"),
        ]

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual == expected
    }

    def "parse with duplicated lines"() {
        given:
        String input = "unbindall\n" +
                "say ciao ciao\n" +
                "say ciao ciao\n"

        List<ParsedCfgCommand> expected = [
                new ParsedCfgCommand("unbindall"),
                new ParsedCfgCommand("say", null, "ciao ciao"),
        ]

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual == expected
    }
}
