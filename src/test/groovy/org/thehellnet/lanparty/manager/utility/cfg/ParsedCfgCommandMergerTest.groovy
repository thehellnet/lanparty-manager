package org.thehellnet.lanparty.manager.utility.cfg

import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand
import org.thehellnet.lanparty.manager.settings.CfgSettings
import spock.lang.Specification
import spock.lang.Unroll

class ParsedCfgCommandMergerTest extends Specification {

    @Unroll
    def "mergeWithTournamentCfg: \"#playerCfgCommands\" -  \"#tournamentCfgCommands\""(List<ParsedCfgCommand> playerCfgCommands, List<ParsedCfgCommand> tournamentCfgCommands, List<ParsedCfgCommand> expected) {
        given:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandMerger(playerCfgCommands).mergeWithTournamentCfg(tournamentCfgCommands)

        expect:
        actual == expected

        where:
        playerCfgCommands | tournamentCfgCommands | expected
        null              | null                  | []
        null              | []                    | []
        []                | null                  | []
        []                | []                    | []
    }

    def "mergeTournamentWithPlayer with empty player"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgSettings.UNBINDALL, CfgSettings.BIND_EXEC, CfgSettings.BIND_DUMP]
        List<ParsedCfgCommand> playerCfgCommands = []
        List<ParsedCfgCommand> expected = [CfgSettings.UNBINDALL, CfgSettings.BIND_EXEC, CfgSettings.BIND_DUMP]

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandMerger(playerCfgCommands).mergeWithTournamentCfg(tournamentCfgCommands)

        then:
        actual == expected
    }

    def "mergeTournamentWithPlayer with forbidden commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgSettings.UNBINDALL, CfgSettings.BIND_EXEC, CfgSettings.BIND_DUMP]
        List<ParsedCfgCommand> playerCfgCommands = [new ParsedCfgCommand("test")]
        List<ParsedCfgCommand> expected = [CfgSettings.UNBINDALL, CfgSettings.BIND_EXEC, CfgSettings.BIND_DUMP]

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandMerger(playerCfgCommands).mergeWithTournamentCfg(tournamentCfgCommands)

        then:
        actual == expected
    }

    def "mergeTournamentWithPlayer with equals commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgSettings.UNBINDALL, new ParsedCfgCommand("bind", "T", "test"), CfgSettings.BIND_DUMP]
        List<ParsedCfgCommand> playerCfgCommands = [new ParsedCfgCommand("bind", "T", "testtest")]
        List<ParsedCfgCommand> expected = [CfgSettings.UNBINDALL, new ParsedCfgCommand("bind", "T", "testtest"), CfgSettings.BIND_DUMP]

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandMerger(playerCfgCommands).mergeWithTournamentCfg(tournamentCfgCommands)

        then:
        actual == expected
    }

    def "mergeTournamentWithPlayer with same cfg"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgSettings.UNBINDALL, CfgSettings.BIND_EXEC, CfgSettings.BIND_DUMP]
        List<ParsedCfgCommand> playerCfgCommands = [CfgSettings.UNBINDALL, CfgSettings.BIND_EXEC, CfgSettings.BIND_DUMP]
        List<ParsedCfgCommand> expected = [CfgSettings.UNBINDALL, CfgSettings.BIND_EXEC, CfgSettings.BIND_DUMP]

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandMerger(playerCfgCommands).mergeWithTournamentCfg(tournamentCfgCommands)

        then:
        actual == expected
    }

    def "mergeTournamentWithPlayer with both forbidden and equals commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [CfgSettings.UNBINDALL, CfgSettings.BIND_EXEC, new ParsedCfgCommand("bind", "T", "test")]
        List<ParsedCfgCommand> playerCfgCommands = [new ParsedCfgCommand("test"), new ParsedCfgCommand("bind", "T", "test")]
        List<ParsedCfgCommand> expected = [CfgSettings.UNBINDALL, CfgSettings.BIND_EXEC, new ParsedCfgCommand("bind", "T", "test")]

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandMerger(playerCfgCommands).mergeWithTournamentCfg(tournamentCfgCommands)

        then:
        actual == expected
    }

    def "mergeTournamentWithPlayer with same commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [
                CfgSettings.UNBINDALL,
                new ParsedCfgCommand("bind", "T", "test"),
                CfgSettings.BIND_EXEC,
                CfgSettings.BIND_DUMP
        ]
        List<ParsedCfgCommand> playerCfgCommands = [
                new ParsedCfgCommand("bind", "T", "onetwo")
        ]
        List<ParsedCfgCommand> expected = [
                CfgSettings.UNBINDALL,
                new ParsedCfgCommand("bind", "T", "onetwo"),
                CfgSettings.BIND_EXEC,
                CfgSettings.BIND_DUMP
        ]

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandMerger(playerCfgCommands).mergeWithTournamentCfg(tournamentCfgCommands)

        then:
        actual == expected
    }

    def "mergeTournamentWithPlayer with same cfg with same commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [
                CfgSettings.UNBINDALL,
                new ParsedCfgCommand("bind", "T", "test"),
                CfgSettings.BIND_EXEC,
                CfgSettings.BIND_DUMP
        ]
        List<ParsedCfgCommand> playerCfgCommands = [
                CfgSettings.UNBINDALL,
                new ParsedCfgCommand("bind", "T", "onetwo"),
                CfgSettings.BIND_EXEC,
                CfgSettings.BIND_DUMP
        ]
        List<ParsedCfgCommand> expected = [
                CfgSettings.UNBINDALL,
                new ParsedCfgCommand("bind", "T", "onetwo"),
                CfgSettings.BIND_EXEC,
                CfgSettings.BIND_DUMP
        ]

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandMerger(playerCfgCommands).mergeWithTournamentCfg(tournamentCfgCommands)

        then:
        actual == expected
    }

    def "mergeTournamentWithPlayer with multiple same commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [
                CfgSettings.UNBINDALL,
                new ParsedCfgCommand("bind", "T", "test"),
                CfgSettings.BIND_EXEC,
                CfgSettings.BIND_DUMP
        ]
        List<ParsedCfgCommand> playerCfgCommands = [
                new ParsedCfgCommand("bind", "T", "onetwo"),
                new ParsedCfgCommand("bind", "T", "onetwothree"),
        ]
        List<ParsedCfgCommand> expected = [
                CfgSettings.UNBINDALL,
                new ParsedCfgCommand("bind", "T", "onetwothree"),
                CfgSettings.BIND_EXEC,
                CfgSettings.BIND_DUMP
        ]

        when:
        List<ParsedCfgCommand> actual = new ParsedCfgCommandMerger(playerCfgCommands).mergeWithTournamentCfg(tournamentCfgCommands)

        then:
        actual == expected
    }
}
