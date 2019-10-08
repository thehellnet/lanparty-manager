package org.thehellnet.lanparty.manager.api.v1;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.thehellnet.lanparty.manager.exception.controller.ControllerException;
import org.thehellnet.lanparty.manager.exception.controller.ResponseStatus;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleCrudException(RuntimeException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (e instanceof ControllerException) {
            ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);
            if (responseStatus != null) {
                httpStatus = responseStatus.value();
            }
        }

        JSONObject responseBody = new JSONObject();
        if (e.getMessage() != null && e.getMessage().length() > 0) {
            responseBody.put("message", e.getMessage());
        }

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody.toString());
    }
}
