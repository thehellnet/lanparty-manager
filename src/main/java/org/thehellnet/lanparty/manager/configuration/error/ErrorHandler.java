package org.thehellnet.lanparty.manager.configuration.error;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.thehellnet.lanparty.manager.exception.controller.LanPartyException;

@ControllerAdvice
public class ErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity handleResourceNotFoundException(ResourceNotFoundException e) {
        return prepareResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleRuntimeException(Exception e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (e instanceof LanPartyException) {
            ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);
            if (responseStatus != null) {
                httpStatus = responseStatus.value();
            }
        }

        return prepareResponseEntity(e, httpStatus);
    }

    private ResponseEntity prepareResponseEntity(Exception e, HttpStatus httpStatus) {
        JSONObject responseBody = new JSONObject();

        if (e.getMessage() != null && e.getMessage().length() > 0) {
            responseBody.put("message", e.getMessage());
        }

        logger.debug(String.format("StatusCode: %d - Message: %s", httpStatus.value(), e.getMessage()));

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody.toString());
    }
}
