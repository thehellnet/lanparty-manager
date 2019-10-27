package org.thehellnet.lanparty.manager.api.v1.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.model.persistence.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/config")
public class ConfigController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);

    @RequestMapping(
            path = { "" , "/{entity}" },
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(@PathVariable Optional<String> entity) {
        JSONObject entityConfig = new JSONObject();


      entityConfig.put(getEntityName(AppUser.class), new JSONObject()
                        .put("label", "User")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("email", "string", false, true, true))
                                .put(prepareField("name", "string", false, true, true))
                                .put(prepareField("barcode", "string", false, true, true))
                                .put(prepareField("appUserRoles", "list", false, false, true))
                        )
                ).put(getEntityName(AppUserToken.class) , new JSONObject()
                        .put("label", "User Token")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("token", "string", true, true, true))
                                .put(prepareField("appUser", "number", true, true, true))
                                .put(prepareField("creationDateTime", "string", true, true, true))
                                .put(prepareField("expirationDateTime", "string", true, true, true))
                        )
                ).put(getEntityName(Cfg.class), new JSONObject()
                        .put("label", "Cfg")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("player", "string", true, true, true))
                                .put(prepareField("game", "number", true, true, true))
                                .put(prepareField("cfgContent", "string", false, false, true))
                        )
                ).put(getEntityName( Game.class), new JSONObject()
                        .put("label", "Game")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("tag", "string", true, true, true))
                                .put(prepareField("name", "number", false, true, true))
                        )
                ).put(getEntityName(GameGametype.class), new JSONObject()
                        .put("label", "Gametypes in game")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("game", "number", false, true, true))
                                .put(prepareField("gametype", "number", false, true, true))
                                .put(prepareField("tag", "string", false, true, true))
                        )
                ).put(getEntityName(GameMap.class), new JSONObject()
                        .put("label", "Map")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("tag", "string", false, true, true))
                                .put(prepareField("name", "string", false, true, true))
                                .put(prepareField("game", "number", false, true, true))
                                .put(prepareField("stock", "boolean", false, true, true))
                        )
                ).put(getEntityName(Gametype.class), new JSONObject()
                        .put("label", "Gametype")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("name", "string", false, true, true))
                        )
                ).put(getEntityName(Match.class), new JSONObject()
                        .put("label", "Match")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("name", "string", false, true, true))
                                .put(prepareField("tournament", "number", false, true, true))
                                .put(prepareField("status", "string", false, true, true))
                                .put(prepareField("playOrder", "number", false, true, true))
                                .put(prepareField("server", "number", false, true, true))
                                .put(prepareField("gameMap", "number", false, true, true))
                                .put(prepareField("gametype", "number", false, true, true))
                                .put(prepareField("localTeam", "number", false, true, true))
                                .put(prepareField("guestTeam", "number", false, true, true))
                        )
                ).put(getEntityName( Player.class), new JSONObject()
                        .put("label", "Player")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("nickname", "string", false, true, true))
                                .put(prepareField("appUser", "number", false, true, true))
                                .put(prepareField("team", "number", false, true, true))
                        )
                ).put(getEntityName(Seat.class), new JSONObject()
                        .put("label", "Seat")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("name", "string", false, true, true))
                                .put(prepareField("ipAddress", "string", false, true, true))
                                .put(prepareField("tournament", "number", false, true, true))
                                .put(prepareField("lastContact", "string", false, true, true))
                                .put(prepareField("player", "number", false, true, true))
                        )
                ).put(getEntityName(Server.class), new JSONObject()
                        .put("label", "Server")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("tag", "string", false, true, true))
                                .put(prepareField("name", "string", false, true, true))
                                .put(prepareField("game", "number", false, true, true))
                                .put(prepareField("address", "string", false, true, true))
                                .put(prepareField("port", "number", false, true, true))
                                .put(prepareField("rconPassword", "string", false, true, true))
                                .put(prepareField("logFile", "string", false, true, true))
                                .put(prepareField("logParsingEnabled", "boolean", false, true, true))
                        )
                ).put(getEntityName(Team.class), new JSONObject()
                        .put("label", "Showcase")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("name", "string", false, true, true))
                                .put(prepareField("mode", "string", false, true, true))
                                .put(prepareField("tournament", "number", false, true, true))
                                .put(prepareField("match", "number", false, true, true))
                                .put(prepareField("lastAddress", "string", true, true, true))
                                .put(prepareField("lastContact", "string", true, true, true))
                        )
                ).put(getEntityName(Team.class), new JSONObject()
                        .put("label", "Team")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("name", "string", false, true, true))
                                .put(prepareField("tournament", "number", false, true, true))
                        )
                ).put(getEntityName(Tournament.class), new JSONObject()
                        .put("label", "Tournament")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("name", "string", false, true, true))
                                .put(prepareField("game", "number", false, true, true))
                                .put(prepareField("status", "string", false, true, true))
                                .put(prepareField("cfg", "string", false, true, true))
                        )
                );


        return !entity.isPresent() ? ResponseEntity.ok(entityConfig.toString()) : ResponseEntity.ok(entityConfig.get(entity.get()).toString());
    }

    private static JSONObject prepareField(String name, String type, boolean readonly, boolean visibleInList, boolean visibleInForm) {
        return new JSONObject()
                .put("name", name)
                .put("type", type)
                .put("readonly", readonly)
                .put("visibleInList", visibleInList)
                .put("visibleInForm", visibleInForm);
    }

    private static String getEntityName(Class clazz){
        String entityName = clazz.getSimpleName();
        return Character.toLowerCase(entityName.charAt(0)) + entityName.substring(1);

    }
}
