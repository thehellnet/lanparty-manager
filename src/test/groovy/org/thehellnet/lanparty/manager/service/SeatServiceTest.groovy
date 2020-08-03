package org.thehellnet.lanparty.manager.service

import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException
import org.thehellnet.lanparty.manager.model.persistence.Player
import org.thehellnet.lanparty.manager.model.persistence.Seat
import spock.lang.Unroll

class SeatServiceTest extends ServiceSpecification {

    @Autowired
    private SeatService seatService

    private Seat seat
    private Seat seat2
    private Player player

    def setup() {
        seat = createSeat()
        seat2 = createSeat2()
        player = createPlayer()
    }

    def "updateLastContact null address"() {
        given:
        String address = null

        when:
        seatService.updateLastContact(address)

        then:
        thrown NotFoundException
    }

    def "updateLastContact empty address"() {
        given:
        String address = ""

        when:
        seatService.updateLastContact(address)

        then:
        thrown NotFoundException
    }

    def "updateLastContact not existing address"() {
        given:
        String address = "not.existing"

        when:
        seatService.updateLastContact(address)

        then:
        thrown NotFoundException
    }

    def "updateLastContact existing address"() {
        given:
        String address = seat.ipAddress
        DateTime lastContact = seat.lastContact

        when:
        seatService.updateLastContact(address)

        then:
        noExceptionThrown()

        when:
        seat = seatRepository.getOne(seat.id)

        then:
        seat.lastContact.isAfter(lastContact)
    }

    @Unroll
    def "updatePlayerInSeats: \"#remoteAddress\" - \"#barcode\""(String remoteAddress, String barcode) {
        when:
        seatService.updatePlayerInSeats(remoteAddress, barcode)

        then:
        thrown NotFoundException

        where:
        remoteAddress  | barcode
        null           | null
        null           | ""
        null           | "0000"
        ""             | null
        ""             | ""
        ""             | "000"
        "not.existing" | null
        "not.existing" | ""
        "not.existing" | "000"
    }

    def "updatePlayerInSeats"() {
        given:
        String remoteAddress = seat.ipAddress
        String remoteAddress2 = seat2.ipAddress
        String barcode = player.appUser.barcode

        when:
        seatService.updatePlayerInSeats(remoteAddress, barcode)

        then:
        noExceptionThrown()

        when:
        seat = seatRepository.getOne(seat.id)

        then:
        seat.player == player
        "Player is in one seat only"(player)

        when:
        seatService.updatePlayerInSeats(remoteAddress2, barcode)

        then:
        noExceptionThrown()

        when:
        seat2 = seatRepository.getOne(seat2.id)

        then:
        seat.player == null
        seat2.player == player
        "Player is in one seat only"(player)
    }
}
