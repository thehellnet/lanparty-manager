package org.thehellnet.lanparty.manager.api.v1.controller;

import org.json.JSONArray;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thehellnet.lanparty.manager.service.RegistrationService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/registration")
public class RegistrationController extends AbstractController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @RequestMapping(
            path = "/getTournaments",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity getTournaments() {
        JSONArray response = registrationService.getTournaments();
        return ResponseEntity.ok(response.toString());
    }
}
