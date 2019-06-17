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
import org.thehellnet.lanparty.manager.model.dto.request.appuser.*;
import org.thehellnet.lanparty.manager.model.dto.response.appuser.*;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken;
import org.thehellnet.lanparty.manager.service.AppUserService;
import org.thehellnet.utility.PasswordUtility;

import javax.servlet.http.HttpServletRequest;
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
    public JsonResponse getAll(HttpServletRequest request, AppUser appUser, @RequestBody AppUserGetAllRequestDTO dto) {
        List<AppUserLight> appUserLightList = appUserService.getAll();
        AppUserGetAllResponseDTO responseDTO = new AppUserGetAllResponseDTO(appUserLightList);
        return JsonResponse.getInstance(responseDTO);
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
    public JsonResponse get(HttpServletRequest request, AppUser appUser, @RequestBody AppUserGetRequestDTO dto) {
        AppUser user = appUserService.get(dto.getId());
        if (user == null) {
            return ErrorCode.prepareResponse(ErrorCode.APPUSER_NOT_FOUND);
        }

        AppUserGetResponseDTO responseDTO = new AppUserGetResponseDTO(user);
        return JsonResponse.getInstance(responseDTO);
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
    public JsonResponse create(HttpServletRequest request, AppUser appUser, @RequestBody AppUserCreateRequestDTO dto) {
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

        AppUserCreateResponseDTO responseDTO = new AppUserCreateResponseDTO(user);
        return JsonResponse.getInstance(responseDTO);
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
    public JsonResponse save(HttpServletRequest request, AppUser appUser, @RequestBody AppUserSaveRequestDTO dto) {
        AppUser user;
        try {
            user = appUserService.save(dto.getId(), dto.getName(), dto.getAppUserRoles());
        } catch (LanPartyManagerException e) {
            logger.error(e.getMessage());
            return ErrorCode.prepareResponse(ErrorCode.APPUSER_NOT_FOUND);
        }

        AppUserSaveResponseDTO responseDTO = new AppUserSaveResponseDTO(user);
        return JsonResponse.getInstance(responseDTO);
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
    public JsonResponse delete(HttpServletRequest request, AppUser appUser, @RequestBody AppUserDeleteRequestDTO dto) {
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
