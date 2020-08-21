package org.thehellnet.lanparty.manager.utility.cfg

import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand
import org.thehellnet.lanparty.manager.constant.CfgConstant
import spock.lang.Specification

class ParsedCfgCommandSpecialEnsurerTest extends Specification {

    public static final ParsedCfgCommand TEST = new ParsedCfgCommand("bind", "T", "test")

    private ParsedCfgCommandUtility<List<ParsedCfgCommand>, List<ParsedCfgCommand>> utility = new ParsedCfgCommandSpecialEnsurer()

    def "with null input"() {
        given:
        List<ParsedCfgCommand> input = null

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual.containsAll(CfgConstant.MINIMAL)
    }

    def "with empty input"() {
        given:
        List<ParsedCfgCommand> input = []

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual.containsAll(CfgConstant.MINIMAL)
    }

    def "with only special"() {
        given:
        List<ParsedCfgCommand> input = CfgConstant.MINIMAL

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual.containsAll(CfgConstant.MINIMAL)
    }

    def "with special"() {
        given:
        List<ParsedCfgCommand> input = [CfgConstant.UNBINDALL, TEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual.containsAll(CfgConstant.MINIMAL)
    }

    def "with name"() {
        given:
        List<ParsedCfgCommand> input = [new ParsedCfgCommand("name", "test"), TEST]

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual.containsAll(CfgConstant.MINIMAL)
    }

    def "ensureMinimals with null"() {
        given:
        List<ParsedCfgCommand> input = null

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual.containsAll(CfgConstant.MINIMAL)
    }

    def "ensureMinimals with empty"() {
        given:
        List<ParsedCfgCommand> input = []

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual.containsAll(CfgConstant.MINIMAL)
    }

    def "ensureMinimals with one minimal"() {
        given:
        List<ParsedCfgCommand> input = [CfgConstant.BIND_DUMP]

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual.containsAll(CfgConstant.MINIMAL)
    }

    def "ensureMinimals with minimal"() {
        given:
        List<ParsedCfgCommand> input = CfgConstant.MINIMAL

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual.containsAll(CfgConstant.MINIMAL)
    }

    def "ensureMinimals with one non minimal"() {
        given:
        List<ParsedCfgCommand> input = [TEST]

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual.containsAll(CfgConstant.MINIMAL)
    }

    def "ensureMinimals with both minimal and non minimal"() {
        given:
        List<ParsedCfgCommand> input = [TEST, CfgConstant.BIND_DUMP]

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        actual.containsAll(CfgConstant.MINIMAL)
    }
}
