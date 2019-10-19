package org.thehellnet.lanparty.manager.service.crud

import org.springframework.beans.factory.annotation.Autowired
import org.thehellnet.lanparty.manager.exception.controller.InvalidDataException
import org.thehellnet.lanparty.manager.exception.controller.NotFoundException
import org.thehellnet.lanparty.manager.exception.controller.UnchangedException
import org.thehellnet.lanparty.manager.model.dto.service.CfgServiceDTO
import org.thehellnet.lanparty.manager.model.persistence.Cfg
import org.thehellnet.lanparty.manager.model.persistence.Game
import org.thehellnet.lanparty.manager.model.persistence.Player
import org.thehellnet.lanparty.manager.service.ServiceSpecification
import org.thehellnet.lanparty.manager.service.crud.CfgCrudService
import spock.lang.Unroll

class CfgCrudServiceTest extends CrudServiceSpecification {

    @Autowired
    private CfgCrudService cfgCrudService

    private Player player
    private Player newPlayer
    private Game game
    private Game newGame

    def setup() {
        player = createPlayer()
        newPlayer = createNewPlayer()
        game = gameRepository.findByTag(GAME_TAG)
        newGame = gameRepository.findByTag(GAME_TAG_NEW)
    }

    @Unroll
    def "create valid user with \"#cfgContent\""(String cfgContent) {
        when:
        CfgServiceDTO serviceDTO = new CfgServiceDTO(player.id, game.id, cfgContent)
        Cfg cfg = cfgCrudService.create(serviceDTO)

        then:
        cfg != null
        cfg.id != null
        cfg.player.id == player.id
        cfg.game.id == game.id
        cfg.cfgContent == ((cfgContent != null) ? cfgContent : "")

        and:
        cfgRepository.findAll().size() == 1

        where:
        cfgContent << [null, "", CFG]
    }

    @Unroll
    def "create with null player, null game and \"#cfgContent\""(String cfgContent) {
        when:
        CfgServiceDTO serviceDTO = new CfgServiceDTO(null, null, cfgContent)
        cfgCrudService.create(serviceDTO)

        then:
        thrown InvalidDataException

        and:
        cfgRepository.findAll().size() == 0

        where:
        cfgContent << [null, "", CFG]
    }

    @Unroll
    def "create with valid player, null game and \"#cfgContent\""(String cfgContent) {
        when:
        CfgServiceDTO serviceDTO = new CfgServiceDTO(player.id, null, cfgContent)
        cfgCrudService.create(serviceDTO)

        then:
        thrown InvalidDataException

        and:
        cfgRepository.findAll().size() == 0

        where:
        cfgContent << [null, "", CFG]
    }

    @Unroll
    def "create with null player, valid game and \"#cfgContent\""(String cfgContent) {
        when:
        CfgServiceDTO serviceDTO = new CfgServiceDTO(null, game.id, cfgContent)
        cfgCrudService.create(serviceDTO)

        then:
        thrown InvalidDataException

        and:
        cfgRepository.findAll().size() == 0

        where:
        cfgContent << [null, "", CFG]
    }

    def "get with existing id"() {
        given:
        Long cfgId = cfgRepository.save(new Cfg(player, game, CFG)).id

        when:
        Cfg cfg = cfgCrudService.read(cfgId)

        then:
        cfg != null
        cfg.id == cfgId
        cfg.player == player
        cfg.game == game
        cfg.cfgContent == CFG
    }

    def "get with not existing id"() {
        given:
        Long cfgId = 12345678

        when:
        cfgCrudService.read(cfgId)

        then:
        thrown NotFoundException
    }

    def "getAll with no cfg"() {
        when:
        List<Cfg> cfgs = cfgCrudService.readAll()

        then:
        cfgs.size() == 0
    }

    def "getAll with one cfg"() {
        given:
        Long cfgId = cfgRepository.save(new Cfg(player, game, CFG)).id

        when:
        List<Cfg> cfgs = cfgCrudService.readAll()

        then:
        cfgs.size() == 1

        when:
        Cfg cfg = cfgs.get(0)

        then:
        cfg != null
        cfg.id == cfgId
        cfg.player == player
        cfg.game == game
        cfg.cfgContent == CFG
    }

    def "update UnchangedException with null player, null game and null cfgContent"() {
        given:
        Long cfgId = cfgRepository.save(new Cfg(this.player, this.game, CFG)).id

        when:
        CfgServiceDTO serviceDTO = new CfgServiceDTO(null, null, null)
        cfgCrudService.update(cfgId, serviceDTO)

        then:
        thrown UnchangedException
    }

    @Unroll
    def "update with null player, null game and '#cfgContent' cfgContent"(String cfgContent) {
        given:
        Long cfgId = cfgRepository.save(new Cfg(this.player, this.game, CFG)).id

        when:
        CfgServiceDTO serviceDTO = new CfgServiceDTO(null, null, cfgContent)
        Cfg cfg = cfgCrudService.update(cfgId, serviceDTO)

        then:
        cfg != null
        cfg.id == cfgId
        cfg.player == this.player
        cfg.game == this.game
        cfg.cfgContent == ((cfgContent != null) ? cfgContent : CFG)

        where:
        cfgContent << ["", CFG, CFG_NEW]
    }

    @Unroll
    def "update with null player, new game and '#cfgContent' cfgContent"(String cfgContent) {
        given:
        Long cfgId = cfgRepository.save(new Cfg(this.player, this.game, CFG)).id

        when:
        CfgServiceDTO serviceDTO = new CfgServiceDTO(null, this.newGame.id, cfgContent)
        Cfg cfg = cfgCrudService.update(cfgId, serviceDTO)

        then:
        cfg != null
        cfg.id == cfgId
        cfg.player == this.player
        cfg.game == this.newGame
        cfg.cfgContent == ((cfgContent != null) ? cfgContent : CFG)

        where:
        cfgContent << [null, "", CFG, CFG_NEW]
    }

    @Unroll
    def "update with new player, null game and '#cfgContent' cfgContent"(String cfgContent) {
        given:
        Long cfgId = cfgRepository.save(new Cfg(this.player, this.game, CFG)).id

        when:
        CfgServiceDTO serviceDTO = new CfgServiceDTO(this.newPlayer.id, null, cfgContent)
        Cfg cfg = cfgCrudService.update(cfgId, serviceDTO)

        then:
        cfg != null
        cfg.id == cfgId
        cfg.player == this.newPlayer
        cfg.game == this.game
        cfg.cfgContent == ((cfgContent != null) ? cfgContent : CFG)

        where:
        cfgContent << [null, "", CFG, CFG_NEW]
    }

    @Unroll
    def "update with new player, new game and '#cfgContent' cfgContent"(String cfgContent) {
        given:
        Long cfgId = cfgRepository.save(new Cfg(this.player, this.game, CFG)).id

        when:
        CfgServiceDTO serviceDTO = new CfgServiceDTO(this.newPlayer.id, this.newGame.id, cfgContent)
        Cfg cfg = cfgCrudService.update(cfgId, serviceDTO)

        then:
        cfg != null
        cfg.id == cfgId
        cfg.player == this.newPlayer
        cfg.game == this.newGame
        cfg.cfgContent == ((cfgContent != null) ? cfgContent : CFG)

        where:
        cfgContent << [null, "", CFG, CFG_NEW]
    }

    def "update with invalid id"() {
        given:
        Long appUserId = 12345678

        when:
        CfgServiceDTO serviceDTO = new CfgServiceDTO(this.newPlayer.id, this.newGame.id, CFG_NEW)
        cfgCrudService.update(appUserId, serviceDTO)

        then:
        thrown NotFoundException
    }

    def "delete"() {
        given:
        Long cfgId = cfgRepository.save(new Cfg(this.player, this.game, CFG)).id

        when:
        cfgCrudService.delete(cfgId)

        then:
        noExceptionThrown()

        and:
        cfgRepository.findAll().size() == 0
    }

    def "delete with not existing ID"() {
        given:
        Long appUserId = 12345678

        when:
        cfgCrudService.delete(appUserId)

        then:
        thrown NotFoundException
    }
}
