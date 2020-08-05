package org.thehellnet.lanparty.manager.service

import org.springframework.beans.factory.annotation.Autowired
import org.thehellnet.lanparty.manager.exception.InvalidDataException
import org.thehellnet.lanparty.manager.exception.NotFoundException
import org.thehellnet.lanparty.manager.model.helper.ParsedCfgCommand
import org.thehellnet.lanparty.manager.model.persistence.Cfg
import org.thehellnet.lanparty.manager.model.persistence.Player
import org.thehellnet.lanparty.manager.model.persistence.Tournament
import org.thehellnet.lanparty.manager.settings.CfgSettings
import org.thehellnet.lanparty.manager.utility.cfg.ParsedCfgCommandParser
import spock.lang.Unroll

class CfgServiceTest extends ServiceSpecification {

    @Autowired
    private CfgService cfgService

    @Unroll
    def "computeCfg thrown InvalidDataException: \"#remoteAddress\" - \"#barcode\""(String remoteAddress, String barcode) {
        when:
        cfgService.computeCfg(remoteAddress, barcode)

        then:
        thrown InvalidDataException

        where:
        remoteAddress | barcode
        null          | null
        null          | ""
        null          | "not.existing"
        null          | APPUSER_BARCODE
        ""            | null
        ""            | ""
        ""            | "not.existing"
        ""            | APPUSER_BARCODE
        "5.6.7.8"     | null
        "5.6.7.8"     | ""
        SEAT_ADDRESS  | null
        SEAT_ADDRESS  | ""
    }

    @Unroll
    def "computeCfg thrown NotFoundException: \"#remoteAddress\" - \"#barcode\""(String remoteAddress, String barcode) {
        when:
        cfgService.computeCfg(remoteAddress, barcode)

        then:
        thrown NotFoundException

        where:
        remoteAddress | barcode
        "5.6.7.8"     | "not.existing"
        "5.6.7.8"     | APPUSER_BARCODE
        SEAT_ADDRESS  | "not.existing"
    }

    def "computeCfg with existing remoteAddress and existing barcode and no Cfg"() {
        given:
        Tournament tournament = createTournament()
        Player player = createPlayer()
        createSeat()

        String remoteAddress = SEAT_ADDRESS
        String barcode = APPUSER_BARCODE

        List<ParsedCfgCommand> expected = new ArrayList<>()
        expected.addAll(CfgSettings.INITIALS)
        expected.addAll(new ParsedCfgCommandParser(tournament.getCfg()).parse())
        expected.addAll(CfgSettings.FINALS)
        expected.addAll(ParsedCfgCommand.prepareName(player.nickname))

        when:
        Cfg cfg = cfgRepository.findByPlayerAndGame(player, tournament.game)
        if (cfg != null) {
            cfgRepository.delete(cfg)
        }

        cfg = cfgRepository.findByPlayerAndGame(player, tournament.game)

        then:
        cfg == null

        when:
        List<ParsedCfgCommand> actual = cfgService.computeCfg(remoteAddress, barcode)

        then:
        actual == expected
    }

    def "computeCfg with existing remoteAddress and existing barcode with Cfg"() {
        given:
        createSeat()

        String remoteAddress = SEAT_ADDRESS
        String barcode = APPUSER_BARCODE

        Tournament tournament = createTournament()
        Cfg cfg = createCfg()

        List<ParsedCfgCommand> playerCfg = new ParsedCfgCommandParser(cfg.cfgContent).parse()
        List<ParsedCfgCommand> tournamentCfg = new ParsedCfgCommandParser(tournament.cfg).parse()

        when:
        List<ParsedCfgCommand> actual = cfgService.computeCfg(remoteAddress, barcode)

        then:
        actual.size() >= tournamentCfg.size()
        actual != playerCfg
    }
//
//    def "test saveCfg"() {
//        given:
//
//        when:
//        // TODO implement stimulus
//        then:
//        // TODO implement assertions
//    }
}
