package org.thehellnet.lanparty.manager.api.v1.controller.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.thehellnet.lanparty.manager.exception.controller.UnauthorizedException;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.service.crud.AppUserTokenCrudServiceOLD;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Order(0)
@Component
public class TokenControllerAspect {

    private static final Logger logger = LoggerFactory.getLogger(TokenControllerAspect.class);

    private final AppUserTokenCrudServiceOLD appUserTokenCrudService;

    @Autowired
    public TokenControllerAspect(AppUserTokenCrudServiceOLD appUserTokenCrudService) {
        this.appUserTokenCrudService = appUserTokenCrudService;
    }

    @Around("@annotation(CheckToken)")
    public Object checkToken(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] params = joinPoint.getArgs();
        logger.debug(joinPoint.getTarget().toString());

        HttpServletRequest servletRequest = (HttpServletRequest) params[0];
        String token = servletRequest.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException();
        }

        AppUser appUser = appUserTokenCrudService.getAppUserByToken(token);
        if (appUser == null) {
            throw new UnauthorizedException();
        }

        params[1] = appUser;

        return joinPoint.proceed(params);
    }
}