package org.thehellnet.lanparty.manager.model.helper

import spock.lang.Specification

class ParsedCfgCommandTest extends Specification {

    private static final String PLAYER_NAME = "test"
    private static final String PLAYER_NAME2 = "test2"

    private static final String MESSAGE = "first messager"
    private static final String MESSAGE2 = "second message"

    def "same with empty values"() {
        given:
        ParsedCfgCommand first = new ParsedCfgCommand()
        ParsedCfgCommand second = new ParsedCfgCommand()

        when:
        boolean isSame = first.same(second)

        then:
        isSame
    }

    def "same with unbindall"() {
        given:
        ParsedCfgCommand first = ParsedCfgCommand.UNBINDALL
        ParsedCfgCommand second = ParsedCfgCommand.UNBINDALL

        when:
        boolean isSame = first.same(second)

        then:
        isSame
    }

    def "same with unbindall impossible"() {
        given:
        ParsedCfgCommand first = ParsedCfgCommand.UNBINDALL.setParam("one")
        ParsedCfgCommand second = ParsedCfgCommand.UNBINDALL.setParam("two")

        when:
        boolean isSame = first.same(second)

        then:
        isSame
    }

    def "same with bind exec"() {
        given:
        ParsedCfgCommand first = ParsedCfgCommand.BIND_EXEC
        ParsedCfgCommand second = ParsedCfgCommand.BIND_EXEC

        when:
        boolean isSame = first.same(second)

        then:
        isSame
    }

    def "same with bind exec impossible"() {
        given:
        ParsedCfgCommand first = ParsedCfgCommand.BIND_EXEC.setParam("one")
        ParsedCfgCommand second = ParsedCfgCommand.BIND_EXEC.setParam("two")

        when:
        boolean isSame = first.same(second)

        then:
        isSame
    }

    def "same with bind dump"() {
        given:
        ParsedCfgCommand first = ParsedCfgCommand.BIND_DUMP
        ParsedCfgCommand second = ParsedCfgCommand.BIND_DUMP

        when:
        boolean isSame = first.same(second)

        then:
        isSame
    }

    def "same with bind dump with impossible"() {
        given:
        ParsedCfgCommand first = ParsedCfgCommand.BIND_DUMP.setParam("one")
        ParsedCfgCommand second = ParsedCfgCommand.BIND_DUMP.setParam("two")

        when:
        boolean isSame = first.same(second)

        then:
        isSame
    }

    def "same with same name"() {
        given:
        ParsedCfgCommand first = ParsedCfgCommand.prepareName(PLAYER_NAME)
        ParsedCfgCommand second = ParsedCfgCommand.prepareName(PLAYER_NAME)

        when:
        boolean isSame = first.same(second)

        then:
        isSame
    }

    def "same with second name"() {
        given:
        ParsedCfgCommand first = ParsedCfgCommand.prepareName(PLAYER_NAME)
        ParsedCfgCommand second = ParsedCfgCommand.prepareName(PLAYER_NAME2)

        when:
        boolean isSame = first.same(second)

        then:
        isSame
    }

    def "same with say"() {
        given:
        ParsedCfgCommand first = ParsedCfgCommand.prepareSay(MESSAGE)
        ParsedCfgCommand second = ParsedCfgCommand.prepareSay(MESSAGE2)

        when:
        boolean isSame = first.same(second)

        then:
        isSame
    }

    def "same with bind"() {
        given:
        ParsedCfgCommand first = new ParsedCfgCommand("bind", "P", "quit")
        ParsedCfgCommand second = new ParsedCfgCommand("bind", "P", "+jump")

        when:
        boolean isSame = first.same(second)

        then:
        isSame
    }

    def "not same with bind"() {
        given:
        ParsedCfgCommand first = new ParsedCfgCommand("bind", "P", "quit")
        ParsedCfgCommand second = new ParsedCfgCommand("bind", "Q", "quit")

        when:
        boolean isSame = first.same(second)

        then:
        !isSame
    }
}
