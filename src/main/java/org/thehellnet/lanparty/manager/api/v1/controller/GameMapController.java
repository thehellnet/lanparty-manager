package org.thehellnet.lanparty.manager.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.dto.JsonResponse;
import org.thehellnet.lanparty.manager.model.dto.request.GameMapListDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Game;
import org.thehellnet.lanparty.manager.model.persistence.GameMap;
import org.thehellnet.lanparty.manager.repository.GameMapRepository;
import org.thehellnet.lanparty.manager.repository.GameRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/api/v1/public/gameMap")
public class GameMapController {

    private final GameRepository gameRepository;
    private final GameMapRepository gameMapRepository;

    @Autowired
    public GameMapController(GameRepository gameRepository, GameMapRepository gameMapRepository) {
        this.gameRepository = gameRepository;
        this.gameMapRepository = gameMapRepository;
    }

    @RequestMapping(
            path = "/list",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CheckToken
    @CheckRoles(Role.READ_PUBLIC)
    @ResponseBody
    public JsonResponse list(AppUser appUser, @RequestBody GameMapListDTO dto) {
        List<GameMap> gameMaps = new ArrayList<>();

        if (dto.gameTag != null) {
            Game game = gameRepository.findByTag(dto.gameTag);
            if (game == null) {
                return JsonResponse.getErrorInstance("Game tag not found");
            }
            gameMaps.addAll(gameMapRepository.findByGame(game));
        } else {
            gameMaps.addAll(gameMapRepository.findAll());
        }

        List<Map<String, Object>> data = new ArrayList<>();
        for (GameMap gameMap : gameMaps) {
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("tag", gameMap.getTag());
            mapData.put("name", gameMap.getName());
            mapData.put("gameTag", gameMap.getGame().getTag());
            mapData.put("stock", gameMap.getStock());
            data.add(mapData);
        }

        return JsonResponse.getInstance(data);
    }
}
