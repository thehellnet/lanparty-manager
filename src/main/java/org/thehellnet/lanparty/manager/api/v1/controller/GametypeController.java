package org.thehellnet.lanparty.manager.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.dto.JsonResponse;
import org.thehellnet.lanparty.manager.model.dto.request.GametypeListDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.GameGametype;
import org.thehellnet.lanparty.manager.model.persistence.Gametype;
import org.thehellnet.lanparty.manager.repository.GameRepository;
import org.thehellnet.lanparty.manager.repository.GametypeRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/api/v1/public/gametype")
public class GametypeController {

    private final GameRepository gameRepository;
    private final GametypeRepository gametypeRepository;

    @Autowired
    public GametypeController(GameRepository gameRepository, GametypeRepository gametypeRepository) {
        this.gameRepository = gameRepository;
        this.gametypeRepository = gametypeRepository;
    }

    @RequestMapping(
            path = "/listPerGame",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CheckToken
    @CheckRoles(Role.READ_PUBLIC)
    @Transactional(readOnly = true)
    @ResponseBody
    public JsonResponse listPerGame(AppUser appUser, @RequestBody GametypeListDTO dto) {
        if (dto.gameTag == null || dto.gameTag.length() == 0) {
            return JsonResponse.getErrorInstance("Invalid game tag");
        }

        Game game = gameRepository.findByTag(dto.gameTag);
        if (game == null) {
            return JsonResponse.getErrorInstance("Game not found");
        }

        List<Gametype> gametypes = gametypeRepository.findByGame(game);

        List<Map<String, Object>> data = new ArrayList<>();
        for (Gametype gametype : gametypes) {
            String gametypeName = gametype.getName();

            GameGametype gameGametype = gametype.
                    getGameGametypes()
                    .stream()
                    .filter(it -> it.getGame().equals(game))
                    .findFirst()
                    .orElse(null);

            String gametypeTag = gameGametype != null ? gameGametype.getTag() : null;

            Map<String, Object> mapData = new HashMap<>();
            mapData.put("name", gametypeName);
            mapData.put("tag", gametypeTag);
            data.add(mapData);
        }

        return JsonResponse.getInstance(data);
    }
}
