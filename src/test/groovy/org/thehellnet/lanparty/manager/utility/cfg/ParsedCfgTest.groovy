package org.thehellnet.lanparty.manager.utility.cfg

import org.thehellnet.lanparty.manager.exception.player.InvalidNamePlayerException
import org.thehellnet.lanparty.manager.utility.UtilitySpecification
import org.thehellnet.utility.StringUtility

class ParsedCfgTest extends UtilitySpecification {

    private String playerNickname

    def setup() {
        playerNickname = createPlayer().nickname
    }

    def "toCommandList with null commands and null nickname"() {
        given:
        List<ParsedCfgCommand> inputCommands = null
        String inputNickname = null

        when:
        new ParsedCfg(inputCommands, inputNickname);

        then:
        thrown InvalidNamePlayerException
    }

    def "toCommandList with empty commands and null nickname"() {
        given:
        List<ParsedCfgCommand> inputCommands = []
        String inputNickname = null

        when:
        new ParsedCfg(inputCommands, inputNickname);

        then:
        thrown InvalidNamePlayerException
    }

    def "toCommandList with null commands and empty nickname"() {
        given:
        List<ParsedCfgCommand> inputCommands = null
        String inputNickname = null

        when:
        new ParsedCfg(inputCommands, inputNickname);

        then:
        thrown InvalidNamePlayerException
    }

    def "toCommandList with empty commands and empty nickname"() {
        given:
        List<ParsedCfgCommand> inputCommands = []
        String inputNickname = ""

        when:
        new ParsedCfg(inputCommands, inputNickname);

        then:
        thrown InvalidNamePlayerException
    }

    def "toCommandList with null commands and empty nickname with spaces"() {
        given:
        List<ParsedCfgCommand> inputCommands = null
        String inputNickname = " "

        when:
        new ParsedCfg(inputCommands, inputNickname);

        then:
        thrown InvalidNamePlayerException
    }

    def "toCommandList with empty commands and empty nickname with spaces"() {
        given:
        List<ParsedCfgCommand> inputCommands = []
        String inputNickname = " "

        when:
        new ParsedCfg(inputCommands, inputNickname);

        then:
        thrown InvalidNamePlayerException
    }

    def "toCommandList with null commands"() {
        given:
        List<ParsedCfgCommand> inputCommands = null

        List<ParsedCfgCommand> expectedCommands = [
                ParsedCfgCommand.UNBINDALL,
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP,
                ParsedCfgCommand.prepareName(playerNickname)
        ]

        when:
        ParsedCfg parsedCfg = new ParsedCfg(inputCommands, playerNickname);
        List<ParsedCfgCommand> actual = parsedCfg.toCommandList()

        then:
        actual == expectedCommands
    }

    def "toCommandList with empty commands"() {
        given:
        List<ParsedCfgCommand> inputCommands = []

        List<ParsedCfgCommand> expectedCommands = [
                ParsedCfgCommand.UNBINDALL,
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP,
                ParsedCfgCommand.prepareName(playerNickname)
        ]

        when:
        ParsedCfg parsedCfg = new ParsedCfg(inputCommands, playerNickname);
        List<ParsedCfgCommand> actual = parsedCfg.toCommandList()

        then:
        actual == expectedCommands
    }

    def "toCommandList with one commands"() {
        given:
        List<ParsedCfgCommand> inputCommands = [
                new ParsedCfgCommand("bind", "P", "quit")
        ]

        List<ParsedCfgCommand> expectedCommands = [
                ParsedCfgCommand.UNBINDALL,
                new ParsedCfgCommand("bind", "P", "quit"),
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP,
                ParsedCfgCommand.prepareName(playerNickname)
        ]

        when:
        ParsedCfg parsedCfg = new ParsedCfg(inputCommands, playerNickname);
        List<ParsedCfgCommand> actual = parsedCfg.toCommandList()

        then:
        actual == expectedCommands
    }

    def "toCommandList with several commands"() {
        given:
        List<ParsedCfgCommand> inputCommands = [
                new ParsedCfgCommand("bind", "P", "quit"),
                new ParsedCfgCommand("bind", "R", "say R"),
                new ParsedCfgCommand("bind", "O", "say O"),
                new ParsedCfgCommand("bind", "V", "say V"),
                new ParsedCfgCommand("bind", "A", "say A")
        ]

        List<ParsedCfgCommand> expectedCommands = [
                ParsedCfgCommand.UNBINDALL,
                new ParsedCfgCommand("bind", "P", "quit"),
                new ParsedCfgCommand("bind", "R", "say R"),
                new ParsedCfgCommand("bind", "O", "say O"),
                new ParsedCfgCommand("bind", "V", "say V"),
                new ParsedCfgCommand("bind", "A", "say A"),
                ParsedCfgCommand.BIND_EXEC,
                ParsedCfgCommand.BIND_DUMP,
                ParsedCfgCommand.prepareName(playerNickname)
        ]

        when:
        ParsedCfg parsedCfg = new ParsedCfg(inputCommands, playerNickname);
        List<ParsedCfgCommand> actual = parsedCfg.toCommandList()

        then:
        actual == expectedCommands
    }

    def "ParsedCfgTest toStringList"() {
        given:
        List<ParsedCfgCommand> inputCommands = [
                new ParsedCfgCommand("bind", "P", "quit"),
                new ParsedCfgCommand("bind", "R", "say R"),
                new ParsedCfgCommand("bind", "O", "say O"),
                new ParsedCfgCommand("bind", "V", "say V"),
                new ParsedCfgCommand("bind", "A", "say A")
        ]

        List<String> expected = [
                ParsedCfgCommand.UNBINDALL.toString(),
                new ParsedCfgCommand("bind", "P", "quit").toString(),
                new ParsedCfgCommand("bind", "R", "say R").toString(),
                new ParsedCfgCommand("bind", "O", "say O").toString(),
                new ParsedCfgCommand("bind", "V", "say V").toString(),
                new ParsedCfgCommand("bind", "A", "say A").toString(),
                ParsedCfgCommand.BIND_EXEC.toString(),
                ParsedCfgCommand.BIND_DUMP.toString(),
                ParsedCfgCommand.prepareName(playerNickname).toString()
        ]

        when:
        ParsedCfg parsedCfg = new ParsedCfg(inputCommands, playerNickname);
        List<String> actual = parsedCfg.toStringList()

        then:
        actual == expected
    }

    def "ParsedCfgTest toString"() {
        given:
        List<ParsedCfgCommand> inputCommands = [
                new ParsedCfgCommand("bind", "P", "quit"),
                new ParsedCfgCommand("bind", "R", "say R"),
                new ParsedCfgCommand("bind", "O", "say O"),
                new ParsedCfgCommand("bind", "V", "say V"),
                new ParsedCfgCommand("bind", "A", "say A")
        ]

        String expected = StringUtility.joinLines([
                ParsedCfgCommand.UNBINDALL.toString(),
                new ParsedCfgCommand("bind", "P", "quit").toString(),
                new ParsedCfgCommand("bind", "R", "say R").toString(),
                new ParsedCfgCommand("bind", "O", "say O").toString(),
                new ParsedCfgCommand("bind", "V", "say V").toString(),
                new ParsedCfgCommand("bind", "A", "say A").toString(),
                ParsedCfgCommand.BIND_EXEC.toString(),
                ParsedCfgCommand.BIND_DUMP.toString(),
                ParsedCfgCommand.prepareName(playerNickname).toString()
        ])

        when:
        ParsedCfg parsedCfg = new ParsedCfg(inputCommands, playerNickname);
        String actual = parsedCfg.toString()

        then:
        actual == expected
    }
}
