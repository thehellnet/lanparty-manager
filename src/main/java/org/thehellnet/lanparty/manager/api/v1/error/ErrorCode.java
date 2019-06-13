package org.thehellnet.lanparty.manager.api.v1.error;

import org.thehellnet.lanparty.manager.model.dto.JsonResponse;

public enum ErrorCode {
    GENERIC,

    APPUSER_NOT_FOUND,
    APPUSER_NOT_ENABLED,
    APPUSER_INVALID_MAIL,
    APPUSER_INVALID_PASSWORD,
    APPUSER_ALREADY_PRESENT,
    APPUSER_PASSWORD_CHANGE_FAILED;

    public static JsonResponse prepareResponse(ErrorCode errorCode) {
        JsonResponse jsonResponse = JsonResponse.getErrorInstance();
        jsonResponse.getErrors().put("code", errorCode.name());
        return jsonResponse;
    }
}
