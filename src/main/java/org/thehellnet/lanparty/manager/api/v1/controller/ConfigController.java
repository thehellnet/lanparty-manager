package org.thehellnet.lanparty.manager.api.v1.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thehellnet.lanparty.manager.model.persistence.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/config")
public class ConfigController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create() {
        JSONObject responseBody = new JSONObject();

        responseBody.put("entities", new JSONArray()
                .put(new JSONObject()
                        .put("name", AppUser.class.getSimpleName())
                        .put("label", "User")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("email", "string", false, true, true))
                                .put(prepareField("name", "string", false, true, true))
                                .put(prepareField("barcode", "string", false, true, true))
                                .put(prepareField("appUserRoles", "list", false, false, true))
                        )
                )
                .put(new JSONObject()
                        .put("name", AppUserToken.class.getSimpleName())
                        .put("label", "User Token")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("token", "string", true, true, true))
                                .put(prepareField("appUser", "number", true, true, true))
                                .put(prepareField("creationDateTime", "string", true, true, true))
                                .put(prepareField("expirationDateTime", "string", true, true, true))
                        )
                )
                .put(new JSONObject()
                        .put("name", Cfg.class.getSimpleName())
                        .put("label", "Cfg")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("player", "string", true, true, true))
                                .put(prepareField("game", "number", true, true, true))
                                .put(prepareField("cfgContent", "string", false, false, true))
                        )
                )
                .put(new JSONObject()
                        .put("name", Game.class.getSimpleName())
                        .put("label", "Game")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("tag", "string", true, true, true))
                                .put(prepareField("name", "number", false, true, true))
                        )
                )
                .put(new JSONObject()
                        .put("name", GameGametype.class.getSimpleName())
                        .put("label", "Gametypes in game")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("game", "number", false, true, true))
                                .put(prepareField("gametype", "number", false, true, true))
                                .put(prepareField("tag", "string", false, true, true))
                        )
                )
                .put(new JSONObject()
                        .put("name", GameMap.class.getSimpleName())
                        .put("label", "Map")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("tag", "string", false, true, true))
                                .put(prepareField("name", "string", false, true, true))
                                .put(prepareField("game", "number", false, true, true))
                                .put(prepareField("stock", "boolean", false, true, true))
                        )
                )
                .put(new JSONObject()
                        .put("name", Gametype.class.getSimpleName())
                        .put("label", "Gametype")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("name", "string", false, true, true))
                        )
                )
                .put(new JSONObject()
                        .put("name", Match.class.getSimpleName())
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
                )
                .put(new JSONObject()
                        .put("name", Player.class.getSimpleName())
                        .put("label", "Player")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("nickname", "string", false, true, true))
                                .put(prepareField("appUser", "number", false, true, true))
                                .put(prepareField("team", "number", false, true, true))
                        )
                )
                .put(new JSONObject()
                        .put("name", Seat.class.getSimpleName())
                        .put("label", "Seat")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("name", "string", false, true, true))
                                .put(prepareField("ipAddress", "string", false, true, true))
                                .put(prepareField("tournament", "number", false, true, true))
                                .put(prepareField("lastContact", "string", false, true, true))
                                .put(prepareField("player", "number", false, true, true))
                        )
                )
                .put(new JSONObject()
                        .put("name", Server.class.getSimpleName())
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
                )
                .put(new JSONObject()
                        .put("name", Team.class.getSimpleName())
                        .put("label", "Team")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("name", "string", false, true, true))
                                .put(prepareField("tournament", "number", false, true, true))
                        )
                )
                .put(new JSONObject()
                        .put("name", Tournament.class.getSimpleName())
                        .put("label", "Tournament")
                        .put("fields", new JSONArray()
                                .put(prepareField("id", "number", true, true, true))
                                .put(prepareField("name", "string", false, true, true))
                                .put(prepareField("game", "number", false, true, true))
                                .put(prepareField("status", "string", false, true, true))
                                .put(prepareField("cfg", "string", false, true, true))
                        )
                )
        );

        return ResponseEntity.ok(responseBody.toString());
    }

    private static JSONObject prepareField(String name, String type, boolean readonly, boolean visibleInList, boolean visibleInForm) {
        return new JSONObject()
                .put("name", name)
                .put("type", type)
                .put("readonly", readonly)
                .put("visibleInList", visibleInList)
                .put("visibleInForm", visibleInForm);
    }

}