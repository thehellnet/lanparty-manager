package org.thehellnet.lanparty.manager.utility.cfg

import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand
import org.thehellnet.lanparty.manager.constant.CfgConstant
import spock.lang.Specification
import spock.lang.Unroll

class ParsedCfgCommandMergerTest extends Specification {

    private static final ParsedCfgCommand TEST = new ParsedCfgCommand("test")
    private static final ParsedCfgCommand BIND_TEST_TEST = new ParsedCfgCommand("bind", "T", "test")
    private static final ParsedCfgCommand BIND_TEST_RETEST = new ParsedCfgCommand("bind", "T", "retest")
    private static final ParsedCfgCommand BIND_TEST_RERETEST = new ParsedCfgCommand("bind", "T", "reretest")

    private ParsedCfgCommandMerger utility = new ParsedCfgCommandMerger()

    @Unroll
    def "#tournamentCfgCommands - #playerCfgCommands - #overrideCfgCommands"(List<ParsedCfgCommand> tournamentCfgCommands,
                                                                             List<ParsedCfgCommand> playerCfgCommands,
                                                                             List<ParsedCfgCommand> overrideCfgCommands) {
        given:
        ParsedCfgCommandMerger.MergeDTO mergeDTO = new ParsedCfgCommandMerger.MergeDTO(tournamentCfgCommands, playerCfgCommands, overrideCfgCommands)

        when:
        List<ParsedCfgCommand> actual = utility.elaborate(mergeDTO)

        then:
        noExceptionThrown()
        actual != null
        actual.isEmpty()

        where:
        tournamentCfgCommands | playerCfgCommands | overrideCfgCommands
        null                  | null              | null
        null                  | null              | []
        null                  | []                | null
        null                  | []                | []
        []                    | null              | null
        []                    | null              | []
        []                    | []                | null
        []                    | []                | []
    }

    def "with empty player"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgConstant.UNBINDALL, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> playerCfgCommands = []
        List<ParsedCfgCommand> overrideCfgCommands = []
        List<ParsedCfgCommand> expected = [CfgConstant.UNBINDALL, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]

        when:
        ParsedCfgCommandMerger.MergeDTO mergeDTO = new ParsedCfgCommandMerger.MergeDTO(tournamentCfgCommands, playerCfgCommands, overrideCfgCommands)
        List<ParsedCfgCommand> actual = utility.elaborate(mergeDTO)

        then:
        actual == expected
    }

    def "with forbidden commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgConstant.UNBINDALL, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> playerCfgCommands = [BIND_TEST_TEST]
        List<ParsedCfgCommand> overrideCfgCommands = []
        List<ParsedCfgCommand> expected = [CfgConstant.UNBINDALL, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]

        when:
        ParsedCfgCommandMerger.MergeDTO mergeDTO = new ParsedCfgCommandMerger.MergeDTO(tournamentCfgCommands, playerCfgCommands, overrideCfgCommands)
        List<ParsedCfgCommand> actual = utility.elaborate(mergeDTO)

        then:
        actual == expected
    }

    def "with equals commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgConstant.UNBINDALL, BIND_TEST_TEST, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> playerCfgCommands = [BIND_TEST_RETEST]
        List<ParsedCfgCommand> overrideCfgCommands = []
        List<ParsedCfgCommand> expected = [CfgConstant.UNBINDALL, BIND_TEST_RETEST, CfgConstant.BIND_DUMP]

        when:
        ParsedCfgCommandMerger.MergeDTO mergeDTO = new ParsedCfgCommandMerger.MergeDTO(tournamentCfgCommands, playerCfgCommands, overrideCfgCommands)
        List<ParsedCfgCommand> actual = utility.elaborate(mergeDTO)

        then:
        actual == expected
    }

    def "with same cfg"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgConstant.UNBINDALL, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> playerCfgCommands = [CfgConstant.UNBINDALL, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> overrideCfgCommands = []
        List<ParsedCfgCommand> expected = [CfgConstant.UNBINDALL, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]

        when:
        ParsedCfgCommandMerger.MergeDTO mergeDTO = new ParsedCfgCommandMerger.MergeDTO(tournamentCfgCommands, playerCfgCommands, overrideCfgCommands)
        List<ParsedCfgCommand> actual = utility.elaborate(mergeDTO)

        then:
        actual == expected
    }

    def "with both forbidden and equals commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgConstant.UNBINDALL, CfgConstant.BIND_EXEC, BIND_TEST_TEST]
        List<ParsedCfgCommand> playerCfgCommands = [TEST, BIND_TEST_TEST]
        List<ParsedCfgCommand> overrideCfgCommands = []
        List<ParsedCfgCommand> expected = [CfgConstant.UNBINDALL, CfgConstant.BIND_EXEC, BIND_TEST_TEST]

        when:
        ParsedCfgCommandMerger.MergeDTO mergeDTO = new ParsedCfgCommandMerger.MergeDTO(tournamentCfgCommands, playerCfgCommands, overrideCfgCommands)
        List<ParsedCfgCommand> actual = utility.elaborate(mergeDTO)

        then:
        actual == expected
    }

    def "with same commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgConstant.UNBINDALL, BIND_TEST_TEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> playerCfgCommands = [BIND_TEST_RETEST]
        List<ParsedCfgCommand> overrideCfgCommands = []
        List<ParsedCfgCommand> expected = [CfgConstant.UNBINDALL, BIND_TEST_RETEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]

        when:
        ParsedCfgCommandMerger.MergeDTO mergeDTO = new ParsedCfgCommandMerger.MergeDTO(tournamentCfgCommands, playerCfgCommands, overrideCfgCommands)
        List<ParsedCfgCommand> actual = utility.elaborate(mergeDTO)

        then:
        actual == expected
    }

    def "with same cfg with same commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgConstant.UNBINDALL, BIND_TEST_TEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> playerCfgCommands = [CfgConstant.UNBINDALL, BIND_TEST_RETEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> overrideCfgCommands = []
        List<ParsedCfgCommand> expected = [CfgConstant.UNBINDALL, BIND_TEST_RETEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]

        when:
        ParsedCfgCommandMerger.MergeDTO mergeDTO = new ParsedCfgCommandMerger.MergeDTO(tournamentCfgCommands, playerCfgCommands, overrideCfgCommands)
        List<ParsedCfgCommand> actual = utility.elaborate(mergeDTO)

        then:
        actual == expected
    }

    def "with multiple same commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgConstant.UNBINDALL, BIND_TEST_TEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> playerCfgCommands = [BIND_TEST_RETEST, BIND_TEST_RERETEST]
        List<ParsedCfgCommand> overrideCfgCommands = []
        List<ParsedCfgCommand> expected = [CfgConstant.UNBINDALL, BIND_TEST_RERETEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]

        when:
        ParsedCfgCommandMerger.MergeDTO mergeDTO = new ParsedCfgCommandMerger.MergeDTO(tournamentCfgCommands, playerCfgCommands, overrideCfgCommands)
        List<ParsedCfgCommand> actual = utility.elaborate(mergeDTO)

        then:
        actual == expected
    }

    def "with same cfg with same commands and extra override"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgConstant.UNBINDALL, BIND_TEST_TEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> playerCfgCommands = [CfgConstant.UNBINDALL, BIND_TEST_RETEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> overrideCfgCommands = [TEST]
        List<ParsedCfgCommand> expected = [CfgConstant.UNBINDALL, BIND_TEST_RETEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]

        when:
        ParsedCfgCommandMerger.MergeDTO mergeDTO = new ParsedCfgCommandMerger.MergeDTO(tournamentCfgCommands, playerCfgCommands, overrideCfgCommands)
        List<ParsedCfgCommand> actual = utility.elaborate(mergeDTO)

        then:
        actual == expected
    }

    def "with same cfg with same commands and same override"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgConstant.UNBINDALL, BIND_TEST_TEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> playerCfgCommands = [CfgConstant.UNBINDALL, BIND_TEST_RETEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> overrideCfgCommands = [BIND_TEST_RERETEST]
        List<ParsedCfgCommand> expected = [CfgConstant.UNBINDALL, BIND_TEST_RERETEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]

        when:
        ParsedCfgCommandMerger.MergeDTO mergeDTO = new ParsedCfgCommandMerger.MergeDTO(tournamentCfgCommands, playerCfgCommands, overrideCfgCommands)
        List<ParsedCfgCommand> actual = utility.elaborate(mergeDTO)

        then:
        actual == expected
    }

    def "with same cfg with same commands and override equals to tournament"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgConstant.UNBINDALL, BIND_TEST_TEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> playerCfgCommands = [CfgConstant.UNBINDALL, BIND_TEST_RETEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> overrideCfgCommands = [BIND_TEST_TEST]
        List<ParsedCfgCommand> expected = [CfgConstant.UNBINDALL, BIND_TEST_TEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]

        when:
        ParsedCfgCommandMerger.MergeDTO mergeDTO = new ParsedCfgCommandMerger.MergeDTO(tournamentCfgCommands, playerCfgCommands, overrideCfgCommands)
        List<ParsedCfgCommand> actual = utility.elaborate(mergeDTO)

        then:
        actual == expected
    }

    def "with same cfg with same commands and override with multiple equals"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgConstant.UNBINDALL, BIND_TEST_TEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> playerCfgCommands = [CfgConstant.UNBINDALL, BIND_TEST_RETEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> overrideCfgCommands = [BIND_TEST_TEST, BIND_TEST_TEST]
        List<ParsedCfgCommand> expected = [CfgConstant.UNBINDALL, BIND_TEST_TEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]

        when:
        ParsedCfgCommandMerger.MergeDTO mergeDTO = new ParsedCfgCommandMerger.MergeDTO(tournamentCfgCommands, playerCfgCommands, overrideCfgCommands)
        List<ParsedCfgCommand> actual = utility.elaborate(mergeDTO)

        then:
        actual == expected
    }

    def "with same cfg with same commands and override with multiple should take last"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgConstant.UNBINDALL, BIND_TEST_TEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> playerCfgCommands = [CfgConstant.UNBINDALL, BIND_TEST_RETEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]
        List<ParsedCfgCommand> overrideCfgCommands = [BIND_TEST_TEST, BIND_TEST_RERETEST]
        List<ParsedCfgCommand> expected = [CfgConstant.UNBINDALL, BIND_TEST_RERETEST, CfgConstant.BIND_EXEC, CfgConstant.BIND_DUMP]

        when:
        ParsedCfgCommandMerger.MergeDTO mergeDTO = new ParsedCfgCommandMerger.MergeDTO(tournamentCfgCommands, playerCfgCommands, overrideCfgCommands)
        List<ParsedCfgCommand> actual = utility.elaborate(mergeDTO)

        then:
        actual == expected
    }
}
