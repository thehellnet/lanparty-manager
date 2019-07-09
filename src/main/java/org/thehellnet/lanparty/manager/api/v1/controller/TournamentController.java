package org.thehellnet.lanparty.manager.api.v1.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.api.v1.error.ErrorCode;
import org.thehellnet.lanparty.manager.exception.game.GameNotFoundException;
import org.thehellnet.lanparty.manager.exception.tournament.TournamentAlreadyExistsException;
import org.thehellnet.lanparty.manager.exception.tournament.TournamentInvalidNameException;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.dto.JsonResponse;
import org.thehellnet.lanparty.manager.model.dto.light.TournamentLight;
import org.thehellnet.lanparty.manager.model.dto.request.crud.GetAllCrudRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.crud.GetCrudRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.crud.create.TournamentCreateCrudRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.response.tournament.TournamentCreateResponseDTO;
import org.thehellnet.lanparty.manager.model.dto.response.tournament.TournamentGetAllResponseDTO;
import org.thehellnet.lanparty.manager.model.dto.response.tournament.TournamentGetResponseDTO;
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

    @RequestMapping(
            path = "/getAll",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CheckToken
    @CheckRoles(Role.TOURNAMENT_VIEW)
    @ResponseBody
    public JsonResponse getAll(HttpServletRequest request, AppUser appUser, @RequestBody GetAllCrudRequestDTO dto) {
        List<Tournament> tournamentList = tournamentService.getAll();

        List<TournamentLight> tournamentLights = new ArrayList<>();

        for (Tournament tournament : tournamentList) {
            TournamentLight appUserLight = new TournamentLight(tournament);
            tournamentLights.add(appUserLight);
        }

        TournamentGetAllResponseDTO responseDTO = new TournamentGetAllResponseDTO(tournamentLights);
        return JsonResponse.getInstance(responseDTO);
    }

    @RequestMapping(
            path = "/get",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CheckToken
    @CheckRoles(Role.TOURNAMENT_VIEW)
    @ResponseBody
    public JsonResponse get(HttpServletRequest request, AppUser appUser, @RequestBody GetCrudRequestDTO dto) {
        Tournament tournament = tournamentService.get(dto.getId());
        if (tournament == null) {
            return ErrorCode.prepareResponse(ErrorCode.TOURNAMENT_NOT_FOUND);
        }

        TournamentGetResponseDTO responseDTO = new TournamentGetResponseDTO(tournament);
        return JsonResponse.getInstance(responseDTO);
    }

    @RequestMapping(
            path = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CheckToken
    @CheckRoles(Role.TOURNAMENT_ADMIN)
    @ResponseBody
    public JsonResponse create(HttpServletRequest request, AppUser appUser, @RequestBody TournamentCreateCrudRequestDTO dto) {
        Tournament tournament;

        try {
            tournament = tournamentService.create(dto.getName(), dto.getGameId());
        } catch (GameNotFoundException e) {
            return ErrorCode.prepareResponse(ErrorCode.GAME_NOT_FOUND);
        } catch (TournamentInvalidNameException e) {
            return ErrorCode.prepareResponse(ErrorCode.TOURNAMENT_INVALID_NAME);
        } catch (TournamentAlreadyExistsException e) {
            return ErrorCode.prepareResponse(ErrorCode.TOURNAMENT_ALREADY_EXISTS);
        }

        TournamentCreateResponseDTO responseDTO = new TournamentCreateResponseDTO(tournament);
        return JsonResponse.getInstance(responseDTO);
    }

    //    @RequestMapping(
//            path = "/save",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @CheckToken
//    @CheckRoles(Role.APPUSER_ADMIN)
//    @ResponseBody
//    public JsonResponse save(HttpServletRequest request, AppUser appUser, @RequestBody AppUserSaveCrudRequestDTO dto) {
//        AppUser user;
//        try {
//            user = appUserService.save(dto.getId(), dto.getName(), dto.getAppUserRoles());
//        } catch (LanPartyManagerException e) {
//            logger.error(e.getMessage());
//            return ErrorCode.prepareResponse(ErrorCode.APPUSER_NOT_FOUND);
//        }
//
//        AppUserSaveResponseDTO responseDTO = new AppUserSaveResponseDTO(user);
//        return JsonResponse.getInstance(responseDTO);
//    }
//
//    @RequestMapping(
//            path = "/delete",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @CheckToken
//    @CheckRoles(Role.APPUSER_ADMIN)
//    @ResponseBody
//    public JsonResponse delete(HttpServletRequest request, AppUser appUser, @RequestBody DeleteCrudRequestDTO dto) {
//        try {
//            appUserService.delete(dto.getId());
//        } catch (AppUserNotFoundException e) {
//            logger.error(e.getMessage());
//            return ErrorCode.prepareResponse(ErrorCode.APPUSER_NOT_FOUND);
//        }
//
//        return JsonResponse.getInstance();
//    }
}
