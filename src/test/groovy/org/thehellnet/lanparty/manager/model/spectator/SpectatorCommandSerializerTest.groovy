package org.thehellnet.lanparty.manager.model.spectator

import org.thehellnet.lanparty.manager.exception.InvalidDataException
import spock.lang.Specification

class SpectatorCommandSerializerTest extends Specification {

    def "serialize with null input"() {
        given:
        SpectatorCommand input = null

        when:
        SpectatorCommandSerializer.serialize(input)

        then:
        thrown InvalidDataException
    }

    def "serialize input \"#input\" expected \"#expected\""(SpectatorCommand input, String expected) {
        when:
        String actual = SpectatorCommandSerializer.serialize(input)

        then:
        actual == expected

        where:
        input                                                      | expected
        new SpectatorCommand(SpectatorCommandAction.JOIN_SPECTATE) | "{\"action\":\"JoinSpectate\"}"
        new SpectatorCommand(SpectatorCommandAction.SET_READY) | "{\"action\":\"SetReady\"}"
        new SpectatorCommand(SpectatorCommandAction.NEXT_PLAYER) | "{\"action\":\"NextPlayer\"}"
    }
}
