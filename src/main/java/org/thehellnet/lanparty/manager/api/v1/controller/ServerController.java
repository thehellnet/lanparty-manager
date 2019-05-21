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
import org.thehellnet.lanparty.manager.model.dto.request.token.ServerListDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.repository.ServerRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/api/v1/public/server")
public class ServerController {

    private final ServerRepository serverRepository;

    @Autowired
    public ServerController(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @RequestMapping(
            path = "/list",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CheckToken
    @CheckRoles(Role.READ_PRIVATE)
    @ResponseBody
    public JsonResponse list(AppUser appUser, @RequestBody ServerListDTO dto) {
        List<Server> servers = serverRepository.findAll();

        List<Map<String, Object>> data = new ArrayList<>();
        for (Server server : servers) {
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("name", server.getName());
            mapData.put("gameTag", server.getGame().getTag());
            mapData.put("address", server.getAddress());
            mapData.put("port", server.getPort());
            data.add(mapData);
        }

        return JsonResponse.getInstance(data);
    }

//    @RequestMapping(
//            path = "/changeMap",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @CheckToken
//    @CheckRoles(Role.READ_PRIVATE)
//    @ResponseBody
//    public JsonResponse changeMap(AppUser appUser, @RequestBody ServerListDTO dto) {
//        List<Server> servers = serverRepository.findAll();
//
//        List<Map<String, Object>> data = new ArrayList<>();
//        for (Server server : servers) {
//            Map<String, Object> mapData = new HashMap<>();
//            mapData.put("name", server.getName());
//            mapData.put("gameTag", server.getGame().getTag());
//            mapData.put("address", server.getAddress());
//            mapData.put("port", server.getPort());
//            data.add(mapData);
//        }
//
//        return JsonResponse.getInstance(data);
//    }
}
