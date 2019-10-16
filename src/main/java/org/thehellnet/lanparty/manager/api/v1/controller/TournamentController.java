package org.thehellnet.lanparty.manager.api.v1.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.exception.game.GameNotFoundException;
import org.thehellnet.lanparty.manager.exception.tournament.TournamentNotFoundException;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.dto.JsonResponse;
import org.thehellnet.lanparty.manager.model.dto.light.TournamentLight;
import org.thehellnet.lanparty.manager.model.dto.request.crud.DeleteCrudRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.crud.GetAllCrudRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.crud.GetCrudRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.crud.create.TournamentCreateCrudRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.crud.save.TournamentSaveCrudRequestDTO;
import org.thehellnet.lanparty.manager.model.error.ErrorCode;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.service.TournamentService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/api/v1/public/tournament")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

//    @RequestMapping(
//            path = "/getAll",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @CheckToken
//    @CheckRoles(Role.TOURNAMENT_VIEW)
//    @ResponseBody
//    public JsonResponse getAll(HttpServletRequest request, AppUser appUser, @RequestBody GetAllCrudRequestDTO dto) {
//        List<Tournament> tournamentList = tournamentService.getAll();
//
//        List<TournamentLight> tournamentLights = new ArrayList<>();
//
//        for (Tournament tournament : tournamentList) {
//            TournamentLight appUserLight = new TournamentLight(tournament);
//            tournamentLights.add(appUserLight);
//        }
//
//        return JsonResponse.getInstance(tournamentLights);
//    }
//
//    @RequestMapping(
//            path = "/get",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @CheckToken
//    @CheckRoles(Role.TOURNAMENT_VIEW)
//    @ResponseBody
//    public JsonResponse get(HttpServletRequest request, AppUser appUser, @RequestBody GetCrudRequestDTO dto) {
//        Tournament tournament = tournamentService.get(dto.getId());
//        if (tournament == null) {
//            return ErrorCode.prepareResponse(ErrorCode.TOURNAMENT_NOT_FOUND);
//        }
//
//        return JsonResponse.getInstance(tournament);
//    }
//
//    @RequestMapping(
//            path = "/create",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @CheckToken
//    @CheckRoles(Role.TOURNAMENT_ADMIN)
//    @ResponseBody
//    public JsonResponse create(HttpServletRequest request, AppUser appUser, @RequestBody TournamentCreateCrudRequestDTO dto) {
//        Tournament tournament;
//
////        tournament = tournamentService.create(dto.getName(), dto.getGame());
//
//        return JsonResponse.getInstance(tournament);
//    }
//
//    @RequestMapping(
//            path = "/save",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @CheckToken
//    @CheckRoles(Role.TOURNAMENT_ADMIN)
//    @ResponseBody
//    public JsonResponse save(HttpServletRequest request, AppUser appUser, @RequestBody TournamentSaveCrudRequestDTO dto) {
//        Tournament tournament;
//
////        tournament = tournamentService.save(dto.getId(), dto.getName(), dto.getGameId(), dto.getStatusName(), dto.getCfg());
//
//        return JsonResponse.getInstance(tournament);
//    }
//
//    @RequestMapping(
//            path = "/delete",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @CheckToken
//    @CheckRoles(Role.TOURNAMENT_ADMIN)
//    @ResponseBody
//    public JsonResponse delete(HttpServletRequest request, AppUser appUser, @RequestBody DeleteCrudRequestDTO dto) {
//        try {
//            tournamentService.delete(dto.getId());
//        } catch (TournamentNotFoundException e) {
//            return ErrorCode.prepareResponse(ErrorCode.TOURNAMENT_NOT_FOUND);
//        }
//
//        return JsonResponse.getInstance();
//    }
}
