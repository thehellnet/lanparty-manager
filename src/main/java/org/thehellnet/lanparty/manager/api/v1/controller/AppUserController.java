package org.thehellnet.lanparty.manager.api.v1.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.dto.request.appuser.LoginAppUserRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.response.appuser.LoginAppUserResponseDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.service.LoginService;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public/v1/appUser")
public class AppUserController extends AbstractController {

    private final LoginService loginService;

    public AppUserController(LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping(
            path = "/login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity login(@RequestBody LoginAppUserRequestDTO dto) {
        AppUser appUser = loginService.findByEmailAndPassword(dto.email, dto.password);
        AppUserToken appUserToken = loginService.newToken(appUser);

        LoginAppUserResponseDTO body = new LoginAppUserResponseDTO();
        body.id = appUserToken.getId();
        body.token = appUserToken.getToken();
        body.expiration = appUserToken.getExpirationDateTime();
        return ResponseEntity.ok(body);
    }

    @CheckToken
    @CheckRoles(Role.ACTION_LOGIN)
    @RequestMapping(
            path = "/isTokenValid",
            method = RequestMethod.GET
    )
    public ResponseEntity isTokenValid(HttpServletRequest request, AppUser appUser) {
        return ResponseEntity.noContent().build();
    }

}
