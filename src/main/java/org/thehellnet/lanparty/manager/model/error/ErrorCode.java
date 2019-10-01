package org.thehellnet.lanparty.manager.model.error;

import org.thehellnet.lanparty.manager.model.dto.JsonResponse;

public enum ErrorCode {
    GENERIC,

    APPUSER_NOT_FOUND,
    APPUSER_NOT_ENABLED,
    APPUSER_INVALID_MAIL,
    APPUSER_INVALID_PASSWORD,
    APPUSER_ALREADY_PRESENT,
    APPUSER_PASSWORD_CHANGE_FAILED,

    TOURNAMENT_NOT_FOUND,
    TOURNAMENT_INVALID_NAME,
    TOURNAMENT_ALREADY_EXISTS,

    GAME_NOT_FOUND;

    public static JsonResponse prepareResponse(ErrorCode errorCode) {
        JsonResponse jsonResponse = JsonResponse.getErrorInstance();
        jsonResponse.getErrors().put("code", errorCode.name());
        return jsonResponse;
    }
}
