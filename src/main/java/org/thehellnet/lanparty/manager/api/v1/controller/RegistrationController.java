package org.thehellnet.lanparty.manager.api.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.model.dto.request.registration.MailAvailableRegistrationRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.registration.NickAvailableRegistrationRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.registration.RegisterRegistrationRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.response.registration.GetRegistrableTournamentsResponseDTO;
import org.thehellnet.lanparty.manager.model.dto.response.registration.RegisterResponseRegistrationResponseDTO;
import org.thehellnet.lanparty.manager.service.PlayerService;
import org.thehellnet.lanparty.manager.service.TournamentService;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping(
        path = "/api/public/v1/registration",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private final TournamentService tournamentService;
    private final PlayerService playerService;

    @Autowired
    public RegistrationController(TournamentService tournamentService,
                                  PlayerService playerService) {
        this.tournamentService = tournamentService;
        this.playerService = playerService;
    }

    @GetMapping(path = "/get-tournaments")
    public ResponseEntity<Object> getTournaments() {
        logger.info("Registration getTournaments");
        GetRegistrableTournamentsResponseDTO responseDTO = tournamentService.getRegistrableTournaments();
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping(path = "/email-available", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> emailAvailable(@RequestBody MailAvailableRegistrationRequestDTO requestDTO) {
        logger.info("Registration mailAvailable");
        boolean mailAvailable = playerService.emailAvailable(requestDTO.getEmail());
        return ResponseEntity.ok(mailAvailable);
    }

    @PostMapping(path = "/nick-available", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> nickAvailable(@RequestBody NickAvailableRegistrationRequestDTO requestDTO) {
        logger.info("Registration nickAvailable");
        boolean nicknameAvailable = playerService.nicknameAvailable(requestDTO.getNickname());
        return ResponseEntity.ok(nicknameAvailable);
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> register(@RequestBody RegisterRegistrationRequestDTO requestDTO) {
        logger.info("Registration register");
        RegisterResponseRegistrationResponseDTO responseDTO = playerService.register(
                requestDTO.getName(),
                requestDTO.getEmail(),
                requestDTO.getPhone(),
                requestDTO.isPlayer(),
                requestDTO.getNickname()
        );
        return ResponseEntity.ok(responseDTO);
    }
}
