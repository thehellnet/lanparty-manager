package org.thehellnet.lanparty.manager.api.v1.controller.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.service.AppUserTokenService;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Order(0)
@Component
public class TokenControllerAspect {

    private static final Logger logger = LoggerFactory.getLogger(TokenControllerAspect.class);

    private final AppUserTokenService appUserTokenService;

    @Autowired
    public TokenControllerAspect(AppUserTokenService appUserTokenService) {
        this.appUserTokenService = appUserTokenService;
    }

    @Around("@annotation(CheckToken)")
    public Object checkToken(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] params = joinPoint.getArgs();
        logger.debug(joinPoint.getTarget().toString());

        HttpServletRequest servletRequest = (HttpServletRequest) params[0];
        String token = servletRequest.getHeader("X-Auth-Token");
        if (token == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        AppUser appUser = appUserTokenService.getAppUserByToken(token);
        if (appUser == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        params[1] = appUser;

        try {
            return joinPoint.proceed(params);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            logger.error(throwable.getMessage());
            throw throwable;
        }
    }
}