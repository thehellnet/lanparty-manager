package org.thehellnet.lanparty.manager.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.dto.JsonResponse;
import org.thehellnet.lanparty.manager.model.dto.request.AppUserLoginRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.token.appuser.AppUserChangePasswordRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.token.appuser.AppUserGetInfoRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.response.appuser.AppUserGetInfoResponseDTO;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.service.AppUserService;
import org.thehellnet.utility.PasswordUtility;

import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/public/appUser")
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @RequestMapping(
            path = "/login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public JsonResponse login(@RequestBody AppUserLoginRequestDTO dto) {
        AppUser appUser = appUserService.findByEmailAndPassword(dto.email, dto.password);
        if (appUser == null) {
            return JsonResponse.getErrorInstance("User not found");
        }

        if (!appUserService.hasAllRoles(appUser, Role.LOGIN)) {
            return JsonResponse.getErrorInstance("User not enabled");
        }

        AppUserToken appUserToken = appUserService.newToken(appUser);

        Map<String, Object> data = new HashMap<>();
        data.put("token", appUserToken.getToken());
        data.put("expiration", appUserToken.getExpirationDateTime());

        return JsonResponse.getInstance(data);
    }

    @RequestMapping(
            path = "/getInfo",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CheckToken
    @CheckRoles(Role.READ_PRIVATE)
    @ResponseBody
    public JsonResponse getInfo(AppUser appUser, @RequestBody AppUserGetInfoRequestDTO dto) {
        AppUserGetInfoResponseDTO responseDTO = new AppUserGetInfoResponseDTO(
                appUser.toString(),
                appUser.getAppUserRoles()
        );
        return JsonResponse.getInstance(responseDTO);
    }

    @RequestMapping(
            path = "/changePassword",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CheckToken
    @CheckRoles(Role.CHANGE_PASSWORD)
    @ResponseBody
    public JsonResponse changePassword(AppUser appUser, @RequestBody AppUserChangePasswordRequestDTO dto) {
        if (!PasswordUtility.verify(appUser.getPassword(), dto.getOldPassword())) {
            return JsonResponse.getErrorInstance("Invalid Password");
        }

        if (!appUserService.changePassword(appUser, dto.getNewPassword())) {
            return JsonResponse.getErrorInstance("Password change failed");
        }

        return JsonResponse.getInstance();
    }
}
