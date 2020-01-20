package org.thehellnet.lanparty.manager.api.v1.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.model.dto.request.auth.ConfirmAuthRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.auth.LoginAuthRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.auth.RegisterAuthRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.response.auth.LoginAuthResponseDTO;
import org.thehellnet.lanparty.manager.model.dto.response.auth.RegisterAuthResponseDTO;
import org.thehellnet.lanparty.manager.service.AuthService;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/auth")
public class AuthController extends AbstractController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(
            path = "/login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity login(@RequestBody @Valid LoginAuthRequestDTO requestDTO) {
        LoginAuthResponseDTO responseDTO = authService.login(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @RequestMapping(
            path = "/isTokenValid",
            method = RequestMethod.GET
    )
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity isTokenValid() {
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(
            path = "/register",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity register(@RequestBody @Valid RegisterAuthRequestDTO dto) {
        RegisterAuthResponseDTO responseDTO = authService.register(dto);
        return ResponseEntity.ok(responseDTO);
    }

    @RequestMapping(
            path = "/confirm",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity confirm(@RequestBody @Valid ConfirmAuthRequestDTO dto) {
        authService.confirm(dto);
        return ResponseEntity.noContent().build();
    }
}
