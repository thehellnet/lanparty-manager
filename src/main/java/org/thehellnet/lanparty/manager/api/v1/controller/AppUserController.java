package org.thehellnet.lanparty.manager.api.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckRoles;
import org.thehellnet.lanparty.manager.api.v1.controller.aspect.CheckToken;
import org.thehellnet.lanparty.manager.api.v1.error.ErrorCode;
import org.thehellnet.lanparty.manager.exception.LanPartyManagerException;
import org.thehellnet.lanparty.manager.exception.appuser.*;
import org.thehellnet.lanparty.manager.model.constant.Role;
import org.thehellnet.lanparty.manager.model.dto.JsonResponse;
import org.thehellnet.lanparty.manager.model.dto.light.AppUserLight;
import org.thehellnet.lanparty.manager.model.dto.request.appuser.AppUserChangePasswordRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.appuser.AppUserGetInfoRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.appuser.AppUserLoginRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.crud.DeleteCrudRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.crud.GetAllCrudRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.crud.GetCrudRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.crud.create.AppUserCreateCrudRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.request.crud.save.AppUserSaveCrudRequestDTO;
import org.thehellnet.lanparty.manager.model.dto.response.appuser.*;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.service.AppUserService;
import org.thehellnet.utility.PasswordUtility;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/public/appUser")
public class AppUserController {

    private static final Logger logger = LoggerFactory.getLogger(AppUserController.class);

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @RequestMapping(
            path = "/getAll",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @ResponseBody
    public JsonResponse getAll(HttpServletRequest request, AppUser appUser, @RequestBody GetAllCrudRequestDTO dto) {
        List<AppUser> appUserList = appUserService.getAll();

        List<AppUserLight> appUserLights = new ArrayList<>();

        for (AppUser appUser1 : appUserList) {
            AppUserLight appUserLight = new AppUserLight(appUser1);
            appUserLights.add(appUserLight);
        }

        return JsonResponse.getInstance(appUserLights);
    }

    @RequestMapping(
            path = "/get",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CheckToken
    @CheckRoles(Role.APPUSER_VIEW)
    @ResponseBody
    public JsonResponse get(HttpServletRequest request, AppUser appUser, @RequestBody GetCrudRequestDTO dto) {
        AppUser user = appUserService.get(dto.getId());
        if (user == null) {
            return ErrorCode.prepareResponse(ErrorCode.APPUSER_NOT_FOUND);
        }

        return JsonResponse.getInstance(user);
    }

    @RequestMapping(
            path = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @ResponseBody
    public JsonResponse create(HttpServletRequest request, AppUser appUser, @RequestBody AppUserCreateCrudRequestDTO dto) {
        AppUser user;
        try {
            user = appUserService.create(dto.getEmail(), dto.getPassword(), dto.getName());
        } catch (AppUserException e) {
            logger.error(e.getMessage());

            if (e instanceof AppUserInvalidMailException) {
                return ErrorCode.prepareResponse(ErrorCode.APPUSER_INVALID_MAIL);
            } else if (e instanceof AppUserAlreadyPresentException) {
                return ErrorCode.prepareResponse(ErrorCode.APPUSER_ALREADY_PRESENT);
            } else if (e instanceof AppUserInvalidPasswordException) {
                return ErrorCode.prepareResponse(ErrorCode.APPUSER_INVALID_PASSWORD);
            }

            return ErrorCode.prepareResponse(ErrorCode.GENERIC);
        }

        return JsonResponse.getInstance(user);
    }

    @RequestMapping(
            path = "/save",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @ResponseBody
    public JsonResponse save(HttpServletRequest request, AppUser appUser, @RequestBody AppUserSaveCrudRequestDTO dto) {
        AppUser user;
        try {
            user = appUserService.save(dto.getId(), dto.getName(), dto.getAppUserRoles());
        } catch (LanPartyManagerException e) {
            logger.error(e.getMessage());
            return ErrorCode.prepareResponse(ErrorCode.APPUSER_NOT_FOUND);
        }

        return JsonResponse.getInstance(user);
    }

    @RequestMapping(
            path = "/delete",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CheckToken
    @CheckRoles(Role.APPUSER_ADMIN)
    @ResponseBody
    public JsonResponse delete(HttpServletRequest request, AppUser appUser, @RequestBody DeleteCrudRequestDTO dto) {
        try {
            appUserService.delete(dto.getId());
        } catch (AppUserNotFoundException e) {
            logger.error(e.getMessage());
            return ErrorCode.prepareResponse(ErrorCode.APPUSER_NOT_FOUND);
        }

        return JsonResponse.getInstance();
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
            return ErrorCode.prepareResponse(ErrorCode.APPUSER_NOT_FOUND);
        }

        if (!appUserService.hasAllRoles(appUser, Role.LOGIN)) {
            return ErrorCode.prepareResponse(ErrorCode.APPUSER_NOT_ENABLED);
        }

        AppUserToken appUserToken = appUserService.newToken(appUser);

        Map<String, Object> data = new HashMap<>();
        data.put("token", appUserToken.getToken());
        data.put("expiration", appUserToken.getExpirationDateTime());

        return JsonResponse.getInstance(data);
    }

    @Deprecated
    @RequestMapping(
            path = "/getInfo",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CheckToken
    @CheckRoles(Role.READ_PRIVATE)
    @ResponseBody
    public JsonResponse getInfo(HttpServletRequest request, AppUser appUser, @RequestBody AppUserGetInfoRequestDTO dto) {
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
    @CheckRoles(Role.APPUSER_CHANGE_PASSWORD)
    @ResponseBody
    public JsonResponse changePassword(HttpServletRequest request, AppUser appUser, @RequestBody AppUserChangePasswordRequestDTO dto) {
        if (!PasswordUtility.verify(appUser.getPassword(), dto.getOldPassword())) {
            return ErrorCode.prepareResponse(ErrorCode.APPUSER_INVALID_PASSWORD);
        }

        if (!appUserService.changePassword(appUser, dto.getNewPassword())) {
            return ErrorCode.prepareResponse(ErrorCode.APPUSER_PASSWORD_CHANGE_FAILED);
        }

        return JsonResponse.getInstance();
    }
}
