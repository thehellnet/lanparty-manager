package org.thehellnet.lanparty.manager.utility.cfg

import org.thehellnet.lanparty.manager.exception.model.InvalidDataException
import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand
import spock.lang.Specification
import spock.lang.Unroll

class ParsedCfgCommandSerializerTest extends Specification {

    @Unroll
    def "serializeCommand with \"#input\""(ParsedCfgCommand input, String expected) {
        when:
        String actual = ParsedCfgCommandSerializer.serializeCommand(input)

        then:
        actual == expected

        where:
        input                                             | expected
        null                                              | null
        new ParsedCfgCommand("unbindall")                 | "unbindall"
        new ParsedCfgCommand("unbindall", null, "test")   | "unbindall"
        new ParsedCfgCommand("unbindall", "test", null)   | "unbindall"
        new ParsedCfgCommand("unbindall", "test", "test") | "unbindall"
        new ParsedCfgCommand("say", null, "ciao")         | "say \"ciao\""
        new ParsedCfgCommand("say", null, "ciao ciao")    | "say \"ciao ciao\""
        new ParsedCfgCommand("say", null, "ciao ciao")    | "say \"ciao ciao\""
        new ParsedCfgCommand("sensitivity", null, "23")   | "sensitivity \"23\""
        new ParsedCfgCommand("one")                       | null
        new ParsedCfgCommand("one", "two")                | "one two"
        new ParsedCfgCommand("one", "two", "three")       | "one two \"three\""
        new ParsedCfgCommand("one", "two", " three")      | "one two \"three\""
        new ParsedCfgCommand("one", "two", "three ")      | "one two \"three\""
        new ParsedCfgCommand("one", "two", " three ")     | "one two \"three\""
        new ParsedCfgCommand("one", "two", "three four")  | "one two \"three four\""
        new ParsedCfgCommand("one", "two", "three  four") | "one two \"three  four\""
    }

    def "serializeCommand with invalid two items command"() {
        given:
        ParsedCfgCommand input = new ParsedCfgCommand("say")

        when:
        ParsedCfgCommandSerializer.serializeCommand(input)

        then:
        thrown InvalidDataException
    }

    def "serializeCommand with null param command"() {
        given:
        ParsedCfgCommand input = new ParsedCfgCommand("one")

        when:
        ParsedCfgCommandSerializer.serializeCommand(input)

        then:
        thrown InvalidDataException
    }

    def "serialize with null input"() {
        given:
        List<ParsedCfgCommand> input = null
        String expected = ""

        when:
        String actual = new ParsedCfgCommandSerializer(input).serialize()

        then:
        actual == expected
    }

    def "parse with empty input"() {
        given:
        List<ParsedCfgCommand> input = []
        String expected = ""

        when:
        String actual = new ParsedCfgCommandSerializer(input).serialize()

        then:
        actual == expected
    }

    def "parse with single line"() {
        given:
        List<ParsedCfgCommand> input = [
                new ParsedCfgCommand("unbindall")
        ]
        String expected = "unbindall"

        when:
        String actual = new ParsedCfgCommandSerializer(input).serialize()

        then:
        actual == expected
    }

    def "parse with two lines"() {
        given:
        List<ParsedCfgCommand> input = [
                new ParsedCfgCommand("unbindall"),
                new ParsedCfgCommand("say", null, "ciao ciao"),
        ]

        String expected = "unbindall\n" +
                "say \"ciao ciao\""

        when:
        String actual = new ParsedCfgCommandSerializer(input).serialize()

        then:
        actual == expected
    }

}
