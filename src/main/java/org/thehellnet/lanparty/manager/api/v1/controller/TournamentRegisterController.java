package org.thehellnet.lanparty.manager.api.v1.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.model.dto.request.tournamentregister.RegisterUserTournamentRegisterRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.response.tournamentregister.RegisterUserTournamentRegisterResponseDTO;
import org.thehellnet.lanparty.manager.service.TournamentRegisterService;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/tournamentRegister")
public class TournamentRegisterController extends AbstractController {

    private final TournamentRegisterService tournamentRegisterService;

    public TournamentRegisterController(TournamentRegisterService tournamentRegisterService) {
        this.tournamentRegisterService = tournamentRegisterService;
    }

    @RequestMapping(
            path = "/registerUser",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity registerUser(@RequestBody @Valid RegisterUserTournamentRegisterRequestDTO requestDTO) {
        RegisterUserTournamentRegisterResponseDTO responseDTO = tournamentRegisterService.registerUser(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
