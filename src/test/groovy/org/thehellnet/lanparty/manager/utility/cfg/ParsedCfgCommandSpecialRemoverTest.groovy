package org.thehellnet.lanparty.manager.utility.cfg


import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand
import org.thehellnet.lanparty.manager.settings.CfgSettings
import spock.lang.Specification

class ParsedCfgCommandSpecialRemoverTest extends Specification {

    public static final ParsedCfgCommand TEST = new ParsedCfgCommand("bind", "T", "test")

    private ParsedCfgCommandUtility<List<ParsedCfgCommand>, List<ParsedCfgCommand>> utility = new ParsedCfgCommandSpecialRemover()

    def "with null input"() {
        given:
        List<ParsedCfgCommand> input = null

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        !actual.containsAll(CfgSettings.MINIMAL)
    }

    def "with empty input"() {
        given:
        List<ParsedCfgCommand> input = []

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        !actual.containsAll(CfgSettings.MINIMAL)
    }

    def "with only special"() {
        given:
        List<ParsedCfgCommand> input = CfgSettings.MINIMAL

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        !actual.containsAll(CfgSettings.MINIMAL)
    }

    def "with special"() {
        given:
        List<ParsedCfgCommand> input = [CfgSettings.UNBINDALL, TEST, CfgSettings.BIND_EXEC, CfgSettings.BIND_DUMP]

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        !actual.containsAll(CfgSettings.MINIMAL)
    }

    def "with name"() {
        given:
        List<ParsedCfgCommand> input = [new ParsedCfgCommand("name", "test"), TEST]

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        !actual.containsAll(CfgSettings.MINIMAL)
    }

    def "ensureMinimals with null"() {
        given:
        List<ParsedCfgCommand> input = null

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        !actual.containsAll(CfgSettings.MINIMAL)
    }

    def "ensureMinimals with empty"() {
        given:
        List<ParsedCfgCommand> input = []

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        !actual.containsAll(CfgSettings.MINIMAL)
    }

    def "ensureMinimals with one minimal"() {
        given:
        List<ParsedCfgCommand> input = [CfgSettings.BIND_DUMP]

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        !actual.containsAll(CfgSettings.MINIMAL)
    }

    def "ensureMinimals with minimal"() {
        given:
        List<ParsedCfgCommand> input = CfgSettings.MINIMAL

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        !actual.containsAll(CfgSettings.MINIMAL)
    }

    def "ensureMinimals with one non minimal"() {
        given:
        List<ParsedCfgCommand> input = [TEST]

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        !actual.containsAll(CfgSettings.MINIMAL)
    }

    def "ensureMinimals with both minimal and non minimal"() {
        given:
        List<ParsedCfgCommand> input = [TEST, CfgSettings.BIND_DUMP]

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(input)

        then:
        !actual.containsAll(CfgSettings.MINIMAL)
    }
}
