package org.thehellnet.lanparty.manager.model.helper

import spock.lang.Specification
import spock.lang.Unroll

class ParsedCfgCommandTest extends Specification {

    private static final String PLAYER_NAME = "test"
    private static final String PLAYER_NAME2 = "test2"

    private static final String MESSAGE = "first messager"
    private static final String MESSAGE2 = "second message"

    @Unroll
    def "same #first #second #expected"(ParsedCfgCommand first, ParsedCfgCommand second, boolean expected) {
        given:
        boolean actual = first.same(second)

        expect:
        actual == expected

        where:
        first                                                                  | second                                                                   | expected
        new ParsedCfgCommand()                                                 | new ParsedCfgCommand()                                                   | true
        ParsedCfgCommand.UNBINDALL                                             | ParsedCfgCommand.UNBINDALL                                               | true
        ParsedCfgCommand.UNBINDALL.replaceParam("param")                       | ParsedCfgCommand.UNBINDALL.replaceParam("param")                         | true
        ParsedCfgCommand.UNBINDALL.replaceParam("param")                       | ParsedCfgCommand.UNBINDALL.replaceParam("param2")                        | true
        ParsedCfgCommand.UNBINDALL.replaceParam("param").replaceArgs("action") | ParsedCfgCommand.UNBINDALL.replaceParam("param").replaceArgs("action")   | true
        ParsedCfgCommand.UNBINDALL.replaceParam("param").replaceArgs("action") | ParsedCfgCommand.UNBINDALL.replaceParam("param").replaceArgs("action2")  | true
        ParsedCfgCommand.UNBINDALL.replaceParam("param").replaceArgs("action") | ParsedCfgCommand.UNBINDALL.replaceParam("param2").replaceArgs("action")  | true
        ParsedCfgCommand.UNBINDALL.replaceParam("param").replaceArgs("action") | ParsedCfgCommand.UNBINDALL.replaceParam("param2").replaceArgs("action2") | true
        ParsedCfgCommand.BIND_EXEC                                             | ParsedCfgCommand.BIND_EXEC                                               | true
        ParsedCfgCommand.BIND_EXEC.replaceParam("param")                       | ParsedCfgCommand.BIND_EXEC.replaceParam("param")                         | true
        ParsedCfgCommand.BIND_EXEC.replaceParam("param")                       | ParsedCfgCommand.BIND_EXEC.replaceParam("param2")                        | false
        ParsedCfgCommand.BIND_EXEC.replaceParam("param").replaceArgs("action") | ParsedCfgCommand.BIND_EXEC.replaceParam("param").replaceArgs("action")   | true
        ParsedCfgCommand.BIND_EXEC.replaceParam("param").replaceArgs("action") | ParsedCfgCommand.BIND_EXEC.replaceParam("param").replaceArgs("action2")  | true
        ParsedCfgCommand.BIND_EXEC.replaceParam("param").replaceArgs("action") | ParsedCfgCommand.BIND_EXEC.replaceParam("param2").replaceArgs("action")  | false
        ParsedCfgCommand.BIND_EXEC.replaceParam("param").replaceArgs("action") | ParsedCfgCommand.BIND_EXEC.replaceParam("param2").replaceArgs("action2") | false
        ParsedCfgCommand.BIND_DUMP                                             | ParsedCfgCommand.BIND_DUMP                                               | true
        ParsedCfgCommand.BIND_DUMP.replaceParam("param")                       | ParsedCfgCommand.BIND_DUMP.replaceParam("param")                         | true
        ParsedCfgCommand.BIND_DUMP.replaceParam("param")                       | ParsedCfgCommand.BIND_DUMP.replaceParam("param2")                        | false
        ParsedCfgCommand.BIND_DUMP.replaceParam("param").replaceArgs("action") | ParsedCfgCommand.BIND_DUMP.replaceParam("param").replaceArgs("action")   | true
        ParsedCfgCommand.BIND_DUMP.replaceParam("param").replaceArgs("action") | ParsedCfgCommand.BIND_DUMP.replaceParam("param").replaceArgs("action2")  | true
        ParsedCfgCommand.BIND_DUMP.replaceParam("param").replaceArgs("action") | ParsedCfgCommand.BIND_DUMP.replaceParam("param2").replaceArgs("action")  | false
        ParsedCfgCommand.BIND_DUMP.replaceParam("param").replaceArgs("action") | ParsedCfgCommand.BIND_DUMP.replaceParam("param2").replaceArgs("action2") | false
        ParsedCfgCommand.prepareName(PLAYER_NAME)                              | ParsedCfgCommand.prepareName(PLAYER_NAME)                                | true
        ParsedCfgCommand.prepareName(PLAYER_NAME).replaceArgs("action")        | ParsedCfgCommand.prepareName(PLAYER_NAME).replaceArgs("action")          | true
        ParsedCfgCommand.prepareName(PLAYER_NAME).replaceArgs("action")        | ParsedCfgCommand.prepareName(PLAYER_NAME).replaceArgs("action2")         | true
        ParsedCfgCommand.prepareName(PLAYER_NAME)                              | ParsedCfgCommand.prepareName(PLAYER_NAME2)                               | true
        ParsedCfgCommand.prepareName(PLAYER_NAME).replaceArgs("action")        | ParsedCfgCommand.prepareName(PLAYER_NAME2).replaceArgs("action")         | true
        ParsedCfgCommand.prepareName(PLAYER_NAME).replaceArgs("action")        | ParsedCfgCommand.prepareName(PLAYER_NAME2).replaceArgs("action2")        | true
        ParsedCfgCommand.prepareSay(MESSAGE)                                   | ParsedCfgCommand.prepareSay(MESSAGE)                                     | true
        ParsedCfgCommand.prepareSay(MESSAGE).replaceArgs("action")             | ParsedCfgCommand.prepareSay(MESSAGE).replaceArgs("action")               | true
        ParsedCfgCommand.prepareSay(MESSAGE).replaceArgs("action")             | ParsedCfgCommand.prepareSay(MESSAGE).replaceArgs("action2")              | true
        ParsedCfgCommand.prepareSay(MESSAGE)                                   | ParsedCfgCommand.prepareSay(MESSAGE2)                                    | true
        ParsedCfgCommand.prepareSay(MESSAGE).replaceArgs("action")             | ParsedCfgCommand.prepareSay(MESSAGE2).replaceArgs("action")              | true
        ParsedCfgCommand.prepareSay(MESSAGE).replaceArgs("action")             | ParsedCfgCommand.prepareSay(MESSAGE2).replaceArgs("action2")             | true
        new ParsedCfgCommand("bind", "P", "quit")                              | new ParsedCfgCommand("bind", "P", "+jump")                               | true
        new ParsedCfgCommand("bind", "P", "quit")                              | new ParsedCfgCommand("bind", "Q", "quit")                                | false
    }
}
