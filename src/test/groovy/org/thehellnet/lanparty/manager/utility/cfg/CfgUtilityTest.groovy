package org.thehellnet.lanparty.manager.utility.cfg


import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand
import spock.lang.Specification
import spock.lang.Unroll

class CfgUtilityTest extends Specification {

    @Unroll
    def "parseCfgFromStringList \"#input\" \"#expected\""(List<String> input, List<ParsedCfgCommand> expected) {
        given:
        List<ParsedCfgCommand> actual = CfgUtility.parseCfgFromStringList(input)

        expect:
        actual == expected

        where:
        input                                   | expected
        []                                      | []
        [""]                                    | []
        [" "]                                   | []
        ["  "]                                  | []
        ["", ""]                                | []
        ["", " "]                               | []
        [" ", ""]                               | []
        [" ", " "]                              | []
        ["test"]                                | [new ParsedCfgCommand("test")]
        ["test "]                               | [new ParsedCfgCommand("test")]
        [" test"]                               | [new ParsedCfgCommand("test")]
        [" test "]                              | [new ParsedCfgCommand("test")]
        ["test", ""]                            | [new ParsedCfgCommand("test")]
        ["test ", ""]                           | [new ParsedCfgCommand("test")]
        [" test", ""]                           | [new ParsedCfgCommand("test")]
        [" test ", ""]                          | [new ParsedCfgCommand("test")]
        ["test", " "]                           | [new ParsedCfgCommand("test")]
        ["test ", " "]                          | [new ParsedCfgCommand("test")]
        [" test", " "]                          | [new ParsedCfgCommand("test")]
        [" test ", " "]                         | [new ParsedCfgCommand("test")]
        ["test", "bind P quit"]                 | [new ParsedCfgCommand("test"), new ParsedCfgCommand("bind", "P", "quit")]
        ["test", "test"]                        | [new ParsedCfgCommand("test")]
        ["test", "test", "test"]                | [new ParsedCfgCommand("test")]
        ["test", "prova", "test"]               | [new ParsedCfgCommand("test"), new ParsedCfgCommand("prova")]
        ["bind P quit", "bind P +jump"]         | [new ParsedCfgCommand("bind", "P", "+jump")]
        ["bind P quit", "test", "bind P +jump"] | [new ParsedCfgCommand("bind", "P", "+jump"), new ParsedCfgCommand("test")]
    }

    @Unroll
    def "mergeTournamentWithPlayer with #tournament tournament and #player player"(List<ParsedCfgCommand> tournamentCfgCommands, List<ParsedCfgCommand> playerCfgCommands, List<ParsedCfgCommand> expected) {
        given:
        List<ParsedCfgCommand> actual = CfgUtility.mergeTournamentWithPlayer(tournamentCfgCommands, playerCfgCommands)

        expect:
        actual == expected

        where:
        tournamentCfgCommands | playerCfgCommands | expected
        null                  | null              | []
        null                  | []                | []
        []                    | null              | []
        []                    | []                | []
    }

    def "mergeTournamentWithPlayer with empty tournament and empty player"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = []
        List<ParsedCfgCommand> playerCfgCommands = []

        List<ParsedCfgCommand> expected = []

        when:
        List<ParsedCfgCommand> actual = CfgUtility.mergeTournamentWithPlayer(tournamentCfgCommands, playerCfgCommands)

        then:
        actual == expected
    }

    def "mergeTournamentWithPlayer with empty player"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [
                ParsedCfgCommand.UNBINDALL,
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        List<ParsedCfgCommand> playerCfgCommands = [
        ]

        List<ParsedCfgCommand> expected = [
                ParsedCfgCommand.UNBINDALL,
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        when:
        List<ParsedCfgCommand> actual = CfgUtility.mergeTournamentWithPlayer(tournamentCfgCommands, playerCfgCommands)

        then:
        actual == expected
    }

    def "mergeTournamentWithPlayer with forbidden commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [
                ParsedCfgCommand.UNBINDALL,
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        List<ParsedCfgCommand> playerCfgCommands = [
                new ParsedCfgCommand("test")
        ]

        List<ParsedCfgCommand> expected = [
                ParsedCfgCommand.UNBINDALL,
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        when:
        List<ParsedCfgCommand> actual = CfgUtility.mergeTournamentWithPlayer(tournamentCfgCommands, playerCfgCommands)

        then:
        actual == expected
    }

    def "mergeTournamentWithPlayer with equals commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [
                ParsedCfgCommand.UNBINDALL,
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        List<ParsedCfgCommand> playerCfgCommands = [
                ParsedCfgCommand.UNBINDALL
        ]

        List<ParsedCfgCommand> expected = [
                ParsedCfgCommand.UNBINDALL,
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        when:
        List<ParsedCfgCommand> actual = CfgUtility.mergeTournamentWithPlayer(tournamentCfgCommands, playerCfgCommands)

        then:
        actual == expected
    }

    def "mergeTournamentWithPlayer with same cfg"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [
                ParsedCfgCommand.UNBINDALL,
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        List<ParsedCfgCommand> playerCfgCommands = [
                ParsedCfgCommand.UNBINDALL,
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        List<ParsedCfgCommand> expected = [
                ParsedCfgCommand.UNBINDALL,
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        when:
        List<ParsedCfgCommand> actual = CfgUtility.mergeTournamentWithPlayer(tournamentCfgCommands, playerCfgCommands)

        then:
        actual == expected
    }

    def "mergeTournamentWithPlayer with both forbidden and equals commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [
                ParsedCfgCommand.UNBINDALL,
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        List<ParsedCfgCommand> playerCfgCommands = [
                new ParsedCfgCommand("test"),
                ParsedCfgCommand.BIND_EXEC
        ]

        List<ParsedCfgCommand> expected = [
                ParsedCfgCommand.UNBINDALL,
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        when:
        List<ParsedCfgCommand> actual = CfgUtility.mergeTournamentWithPlayer(tournamentCfgCommands, playerCfgCommands)

        then:
        actual == expected
    }

    def "mergeTournamentWithPlayer with same commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [
                ParsedCfgCommand.UNBINDALL,
                new ParsedCfgCommand("bind", "P", "quit"),
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        List<ParsedCfgCommand> playerCfgCommands = [
                new ParsedCfgCommand("bind", "P", "+jump")
        ]

        List<ParsedCfgCommand> expected = [
                ParsedCfgCommand.UNBINDALL,
                new ParsedCfgCommand("bind", "P", "+jump"),
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        when:
        List<ParsedCfgCommand> actual = CfgUtility.mergeTournamentWithPlayer(tournamentCfgCommands, playerCfgCommands)

        then:
        actual == expected
    }

    def "mergeTournamentWithPlayer with same cfg with same commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [
                ParsedCfgCommand.UNBINDALL,
                new ParsedCfgCommand("bind", "P", "quit"),
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        List<ParsedCfgCommand> playerCfgCommands = [
                ParsedCfgCommand.UNBINDALL,
                new ParsedCfgCommand("bind", "P", "+jump"),
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        List<ParsedCfgCommand> expected = [
                ParsedCfgCommand.UNBINDALL,
                new ParsedCfgCommand("bind", "P", "+jump"),
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        when:
        List<ParsedCfgCommand> actual = CfgUtility.mergeTournamentWithPlayer(tournamentCfgCommands, playerCfgCommands)

        then:
        actual == expected
    }

    def "mergeTournamentWithPlayer with multiple same commands"() {
        given:
        List<ParsedCfgCommand> tournamentCfgCommands = [
                ParsedCfgCommand.UNBINDALL,
                new ParsedCfgCommand("bind", "P", "quit"),
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        List<ParsedCfgCommand> playerCfgCommands = [
                ParsedCfgCommand.UNBINDALL,
                new ParsedCfgCommand("bind", "P", "+jump"),
                ParsedCfgCommand.BIND_EXEC,
                new ParsedCfgCommand("bind", "P", "+other"),
                ParsedCfgCommand.BIND_DUMP
        ]

        List<ParsedCfgCommand> expected = [
                ParsedCfgCommand.UNBINDALL,
                new ParsedCfgCommand("bind", "P", "+other"),
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        when:
        List<ParsedCfgCommand> actual = CfgUtility.mergeTournamentWithPlayer(tournamentCfgCommands, playerCfgCommands)

        then:
        actual == expected
    }

    def "removeSpecialCommands with null input"() {
        given:
        List<ParsedCfgCommand> input = null

        List<ParsedCfgCommand> expected = []

        when:
        List<ParsedCfgCommand> actual = CfgUtility.removeSpecialCommands(input)

        then:
        actual == expected
    }

    def "removeSpecialCommands with empty input"() {
        given:
        List<ParsedCfgCommand> input = []

        List<ParsedCfgCommand> expected = []

        when:
        List<ParsedCfgCommand> actual = CfgUtility.removeSpecialCommands(input)

        then:
        actual == expected
    }

    def "removeSpecialCommands with only special"() {
        given:
        List<ParsedCfgCommand> input = [
                ParsedCfgCommand.UNBINDALL,
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        List<ParsedCfgCommand> expected = []

        when:
        List<ParsedCfgCommand> actual = CfgUtility.removeSpecialCommands(input)

        then:
        actual == expected
    }

    def "removeSpecialCommands with special"() {
        given:
        List<ParsedCfgCommand> input = [
                ParsedCfgCommand.UNBINDALL,
                new ParsedCfgCommand("bind", "P", "quit"),
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP
        ]

        List<ParsedCfgCommand> expected = [
                new ParsedCfgCommand("bind", "P", "quit"),
        ]

        when:
        List<ParsedCfgCommand> actual = CfgUtility.removeSpecialCommands(input)

        then:
        actual == expected
    }

    def "removeSpecialCommands with name"() {
        given:
        List<ParsedCfgCommand> input = [
                new ParsedCfgCommand("name", "test"),
                new ParsedCfgCommand("bind", "P", "quit")
        ]

        List<ParsedCfgCommand> expected = [
                new ParsedCfgCommand("bind", "P", "quit"),
        ]

        when:
        List<ParsedCfgCommand> actual = CfgUtility.removeSpecialCommands(input)

        then:
        actual == expected
    }

    @Unroll
    def "parseCommand with input \"#a\" and expected \"#b\""(String a, ParsedCfgCommand b) {
        given:
        ParsedCfgCommand actual = CfgUtility.parseCommand(a)

        expect:
        actual == b

        where:
        a                                  | b
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
    }

    @Unroll
    def "ensureRequired with empty cfg: \"#cfg\""(String cfg) {
        expect:
        CfgUtility.ensureRequired(cfg) == CfgUtility.CFG_MINIMAL

        where:
        cfg       | _
        null      | _
        ""        | _
        " "       | _
        "  "      | _
        "   "     | _
        "\n"      | _
        "\r"      | _
        "\n\r"    | _
        "\n \r"   | _
        "\n\r "   | _
        " \n\r"   | _
        " \n \r"  | _
        " \n\r "  | _
        " \n \r " | _
    }

    def "ensureRequired with normal cfg"() {
        given:
        String input = "unbindall\n" +
                "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind . \"exec lanpartytool\"\n" +
                "bind , \"writeconfig lanpartydump\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\""

        String expected = "unbindall\n" +
                "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\"\n" +
                "bind . \"exec lanpartytool\"\n" +
                "bind , \"writeconfig lanpartydump\""

        when:
        String actual = CfgUtility.ensureRequired(input)

        then:
        actual == expected
    }

    def "ensureRequired without unbindall"() {
        given:
        String input = "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind . \"exec lanpartytool\"\n" +
                "bind , \"writeconfig lanpartydump\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\""

        String expected = "unbindall\n" +
                "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\"\n" +
                "bind . \"exec lanpartytool\"\n" +
                "bind , \"writeconfig lanpartydump\""

        when:
        String actual = CfgUtility.ensureRequired(input)

        then:
        actual == expected
    }

    def "ensureRequired without bindings"() {
        given:
        String input = "unbindall\n" +
                "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\""

        String expected = "unbindall\n" +
                "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\"\n" +
                "bind . \"exec lanpartytool\"\n" +
                "bind , \"writeconfig lanpartydump\""

        when:
        String actual = CfgUtility.ensureRequired(input)

        then:
        actual == expected
    }

    def "ensureRequired without bindings and unbinding"() {
        given:
        String input = "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\""

        String expected = "unbindall\n" +
                "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\"\n" +
                "bind . \"exec lanpartytool\"\n" +
                "bind , \"writeconfig lanpartydump\""

        when:
        String actual = CfgUtility.ensureRequired(input)

        then:
        actual == expected
    }

    def "ensureRequired without unbinding"() {
        given:
        String input = "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\"\n" +
                "bind . \"exec lanpartytool\"\n" +
                "bind , \"writeconfig lanpartydump\""

        String expected = "unbindall\n" +
                "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\"\n" +
                "bind . \"exec lanpartytool\"\n" +
                "bind , \"writeconfig lanpartydump\""

        when:
        String actual = CfgUtility.ensureRequired(input)

        then:
        actual == expected
    }

    def "ensureRequired with inverter order"() {
        given:
        String input = "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\"\n" +
                "bind , \"writeconfig lanpartydump\"\n" +
                "bind . \"exec lanpartytool\"\n"

        String expected = "unbindall\n" +
                "bind TAB \"+scores\"\n" +
                "bind ESCAPE \"togglemenu\"\n" +
                "bind SPACE \"+gostand\"\n" +
                "bind 4 \"+smoke\"\n" +
                "bind H \"say Google\"\n" +
                "bind . \"exec lanpartytool\"\n" +
                "bind , \"writeconfig lanpartydump\""

        when:
        String actual = CfgUtility.ensureRequired(input)

        then:
        actual == expected
    }
}
