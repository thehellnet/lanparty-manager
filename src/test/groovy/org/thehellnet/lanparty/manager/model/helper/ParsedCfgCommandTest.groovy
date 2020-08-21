package org.thehellnet.lanparty.manager.model.helper

import org.thehellnet.lanparty.manager.constant.CfgConstant
import org.thehellnet.lanparty.manager.exception.InvalidDataException
import spock.lang.Specification
import spock.lang.Unroll

class ParsedCfgCommandTest extends Specification {

    private static final String PLAYER_NAME = "test"
    private static final String PLAYER_NAME2 = "test2"

    private static final String MESSAGE = "first message"
    private static final String MESSAGE2 = "second message"

    @Unroll
    def "same #first #second #expected"(ParsedCfgCommand first, ParsedCfgCommand second, boolean expected) {
        given:
        boolean actual = first.same(second)

        expect:
        actual == expected

        where:
        first                                                             | second                                                              | expected
        CfgConstant.UNBINDALL                                             | CfgConstant.UNBINDALL                                               | true
        CfgConstant.UNBINDALL.replaceParam("param")                       | CfgConstant.UNBINDALL.replaceParam("param")                         | true
        CfgConstant.UNBINDALL.replaceParam("param")                       | CfgConstant.UNBINDALL.replaceParam("param2")                        | true
        CfgConstant.UNBINDALL.replaceParam("param").replaceArgs("action") | CfgConstant.UNBINDALL.replaceParam("param").replaceArgs("action")   | true
        CfgConstant.UNBINDALL.replaceParam("param").replaceArgs("action") | CfgConstant.UNBINDALL.replaceParam("param").replaceArgs("action2")  | true
        CfgConstant.UNBINDALL.replaceParam("param").replaceArgs("action") | CfgConstant.UNBINDALL.replaceParam("param2").replaceArgs("action")  | true
        CfgConstant.UNBINDALL.replaceParam("param").replaceArgs("action") | CfgConstant.UNBINDALL.replaceParam("param2").replaceArgs("action2") | true
        CfgConstant.BIND_EXEC                                             | CfgConstant.BIND_EXEC                                               | true
        CfgConstant.BIND_EXEC.replaceParam("param")                       | CfgConstant.BIND_EXEC.replaceParam("param")                         | true
        CfgConstant.BIND_EXEC.replaceParam("param")                       | CfgConstant.BIND_EXEC.replaceParam("param2")                        | false
        CfgConstant.BIND_EXEC.replaceParam("param").replaceArgs("action") | CfgConstant.BIND_EXEC.replaceParam("param").replaceArgs("action")   | true
        CfgConstant.BIND_EXEC.replaceParam("param").replaceArgs("action") | CfgConstant.BIND_EXEC.replaceParam("param").replaceArgs("action2")  | true
        CfgConstant.BIND_EXEC.replaceParam("param").replaceArgs("action") | CfgConstant.BIND_EXEC.replaceParam("param2").replaceArgs("action")  | false
        CfgConstant.BIND_EXEC.replaceParam("param").replaceArgs("action") | CfgConstant.BIND_EXEC.replaceParam("param2").replaceArgs("action2") | false
        CfgConstant.BIND_DUMP                                             | CfgConstant.BIND_DUMP                                               | true
        CfgConstant.BIND_DUMP.replaceParam("param")                       | CfgConstant.BIND_DUMP.replaceParam("param")                         | true
        CfgConstant.BIND_DUMP.replaceParam("param")                       | CfgConstant.BIND_DUMP.replaceParam("param2")                        | false
        CfgConstant.BIND_DUMP.replaceParam("param").replaceArgs("action") | CfgConstant.BIND_DUMP.replaceParam("param").replaceArgs("action")   | true
        CfgConstant.BIND_DUMP.replaceParam("param").replaceArgs("action") | CfgConstant.BIND_DUMP.replaceParam("param").replaceArgs("action2")  | true
        CfgConstant.BIND_DUMP.replaceParam("param").replaceArgs("action") | CfgConstant.BIND_DUMP.replaceParam("param2").replaceArgs("action")  | false
        CfgConstant.BIND_DUMP.replaceParam("param").replaceArgs("action") | CfgConstant.BIND_DUMP.replaceParam("param2").replaceArgs("action2") | false
        ParsedCfgCommand.prepareName(PLAYER_NAME)                         | ParsedCfgCommand.prepareName(PLAYER_NAME)                           | true
        ParsedCfgCommand.prepareName(PLAYER_NAME).replaceArgs("action")   | ParsedCfgCommand.prepareName(PLAYER_NAME).replaceArgs("action")     | true
        ParsedCfgCommand.prepareName(PLAYER_NAME).replaceArgs("action")   | ParsedCfgCommand.prepareName(PLAYER_NAME).replaceArgs("action2")    | true
        ParsedCfgCommand.prepareName(PLAYER_NAME)                         | ParsedCfgCommand.prepareName(PLAYER_NAME2)                          | true
        ParsedCfgCommand.prepareName(PLAYER_NAME).replaceArgs("action")   | ParsedCfgCommand.prepareName(PLAYER_NAME2).replaceArgs("action")    | true
        ParsedCfgCommand.prepareName(PLAYER_NAME).replaceArgs("action")   | ParsedCfgCommand.prepareName(PLAYER_NAME2).replaceArgs("action2")   | true
        ParsedCfgCommand.prepareSay(MESSAGE)                              | ParsedCfgCommand.prepareSay(MESSAGE)                                | true
        ParsedCfgCommand.prepareSay(MESSAGE).replaceArgs("action")        | ParsedCfgCommand.prepareSay(MESSAGE).replaceArgs("action")          | true
        ParsedCfgCommand.prepareSay(MESSAGE).replaceArgs("action")        | ParsedCfgCommand.prepareSay(MESSAGE).replaceArgs("action2")         | true
        ParsedCfgCommand.prepareSay(MESSAGE)                              | ParsedCfgCommand.prepareSay(MESSAGE2)                               | true
        ParsedCfgCommand.prepareSay(MESSAGE).replaceArgs("action")        | ParsedCfgCommand.prepareSay(MESSAGE2).replaceArgs("action")         | true
        ParsedCfgCommand.prepareSay(MESSAGE).replaceArgs("action")        | ParsedCfgCommand.prepareSay(MESSAGE2).replaceArgs("action2")        | true
        new ParsedCfgCommand("bind", "P", "quit")                         | new ParsedCfgCommand("bind", "P", "+jump")                          | true
        new ParsedCfgCommand("bind", "P", "quit")                         | new ParsedCfgCommand("bind", "Q", "quit")                           | false
    }

    def "prepareName with null"() {
        given:
        String input = null

        when:
        ParsedCfgCommand.prepareName(input)

        then:
        thrown InvalidDataException
    }

    def "prepareName with empty"() {
        given:
        String input = ""

        when:
        ParsedCfgCommand.prepareName(input)

        then:
        thrown InvalidDataException
    }

    def "prepareName with valid"() {
        given:
        String input = PLAYER_NAME
        ParsedCfgCommand expected = new ParsedCfgCommand("name", null, PLAYER_NAME)

        when:
        ParsedCfgCommand actual = ParsedCfgCommand.prepareName(input)

        then:
        actual == expected
    }

    def "prepareSay with null"() {
        given:
        String input = null

        when:
        ParsedCfgCommand.prepareSay(input)

        then:
        thrown InvalidDataException
    }

    def "prepareSay with empty"() {
        given:
        String input = ""

        when:
        ParsedCfgCommand.prepareSay(input)

        then:
        thrown InvalidDataException
    }

    def "prepareSay with valid"() {
        given:
        String input = MESSAGE
        ParsedCfgCommand expected = new ParsedCfgCommand("say", null, MESSAGE)

        when:
        ParsedCfgCommand actual = ParsedCfgCommand.prepareSay(input)

        then:
        actual == expected
    }

    def "prepareSay with valid and format"() {
        given:
        String input = "Message is %s"
        ParsedCfgCommand expected = new ParsedCfgCommand("say", null, String.format(input, MESSAGE))

        when:
        ParsedCfgCommand actual = ParsedCfgCommand.prepareSay(input, MESSAGE)

        then:
        actual == expected
    }
}
