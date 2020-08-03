package org.thehellnet.lanparty.manager.utility.cfg


import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand
import org.thehellnet.lanparty.manager.settings.CfgSettings
import spock.lang.Specification

class ParsedCfgCommandSanitizerTest extends Specification {


    public static final ParsedCfgCommand TEST = new ParsedCfgCommand("bind", "T", "test")

    def "removeSpecials with null input"() {
        given:
        List<ParsedCfgCommand> input = null
        List<ParsedCfgCommand> expected = []

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandSanitizer(input).removeSpecials()

        then:
        actual == expected
    }

    def "removeSpecials with empty input"() {
        given:
        List<ParsedCfgCommand> input = []
        List<ParsedCfgCommand> expected = []

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandSanitizer(input).removeSpecials()

        then:
        actual == expected
    }

    def "removeSpecials with only special"() {
        given:
        List<ParsedCfgCommand> input = CfgSettings.MINIMAL
        List<ParsedCfgCommand> expected = []

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandSanitizer(input).removeSpecials()

        then:
        actual == expected
    }

    def "removeSpecials with special"() {
        given:
        List<ParsedCfgCommand> input = [
                CfgSettings.UNBINDALL,
                TEST,
                CfgSettings.BIND_EXEC,
                CfgSettings.BIND_DUMP
        ]

        List<ParsedCfgCommand> expected = [
                TEST
        ]

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandSanitizer(input).removeSpecials()

        then:
        actual == expected
    }

    def "removeSpecials with name"() {
        given:
        List<ParsedCfgCommand> input = [
                new ParsedCfgCommand("name", "test"),
                TEST
        ]

        List<ParsedCfgCommand> expected = [
                TEST
        ]

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandSanitizer(input).removeSpecials()

        then:
        actual == expected
    }

    def "ensureMinimals with null"() {
        given:
        List<ParsedCfgCommand> input = null
        List<ParsedCfgCommand> expected = CfgSettings.MINIMAL

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandSanitizer(input).ensureMinimals()

        then:
        actual == expected
    }

    def "ensureMinimals with empty"() {
        given:
        List<ParsedCfgCommand> input = []
        List<ParsedCfgCommand> expected = CfgSettings.MINIMAL

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandSanitizer(input).ensureMinimals()

        then:
        actual == expected
    }

    def "ensureMinimals with one minimal"() {
        given:
        List<ParsedCfgCommand> input = [CfgSettings.BIND_DUMP]
        List<ParsedCfgCommand> expected = CfgSettings.MINIMAL

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandSanitizer(input).ensureMinimals()

        then:
        actual == expected
    }

    def "ensureMinimals with minimal"() {
        given:
        List<ParsedCfgCommand> input = CfgSettings.MINIMAL
        List<ParsedCfgCommand> expected = CfgSettings.MINIMAL

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandSanitizer(input).ensureMinimals()

        then:
        actual == expected
    }

    def "ensureMinimals with one non minimal"() {
        given:
        List<ParsedCfgCommand> input = [TEST]

        List<ParsedCfgCommand> expected = []
        expected.addAll(CfgSettings.INITIALS)
        expected.addAll(input)
        expected.addAll(CfgSettings.FINALS)

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandSanitizer(input).ensureMinimals()

        then:
        actual == expected
    }

    def "ensureMinimals with both minimal and non minimal"() {
        given:
        List<ParsedCfgCommand> input = [TEST, CfgSettings.BIND_DUMP]

        List<ParsedCfgCommand> expected = []
        expected.addAll(CfgSettings.INITIALS)
        expected.add(TEST)
        expected.addAll(CfgSettings.FINALS)

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandSanitizer(input).ensureMinimals()

        then:
        actual == expected
    }
}
