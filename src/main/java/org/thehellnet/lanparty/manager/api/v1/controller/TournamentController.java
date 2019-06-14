package org.thehellnet.lanparty.manager.api.v1.controller;

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
import org.thehellnet.lanparty.manager.model.dto.request.tournament.TournamentListRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.response.tournament.TournamentListResponseDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Tournament;
import org.thehellnet.lanparty.manager.service.TournamentService;

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
            path = "/list",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CheckToken
    @CheckRoles(Role.READ_PUBLIC)
    @ResponseBody
    public JsonResponse list(AppUser appUser, @RequestBody TournamentListRequestDTO dto) {
        List<Tournament> tournaments = tournamentService.getAll();

        List<TournamentListResponseDTO.TournamentDTO> tournamentDTOS = new ArrayList<>();
        for (Tournament tournament : tournaments) {
            TournamentListResponseDTO.TournamentDTO tournamentDTO = new TournamentListResponseDTO.TournamentDTO();
            tournamentDTO.setId(tournament.getId());
            tournamentDTO.setName(tournament.getName());
            tournamentDTO.setGameTag(tournament.getGame().getTag());
            tournamentDTOS.add(tournamentDTO);
        }

        TournamentListResponseDTO responseDTO = new TournamentListResponseDTO(tournamentDTOS);
        return JsonResponse.getInstance(responseDTO);
    }
}
