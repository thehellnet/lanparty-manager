package org.thehellnet.lanparty.manager.api.v1.controller.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.thehellnet.lanparty.manager.exception.controller.UnauthorizedException;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.service.crud.AppUserService;

import java.lang.reflect.Method;

@Aspect
@Order(1)
@Component
public class RolesControllerAspect {
    private static final Logger logger = LoggerFactory.getLogger(TokenControllerAspect.class);

    private final AppUserService appUserService;

    @Autowired
    public RolesControllerAspect(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Around("@annotation(CheckRoles)")
    public Object checkRoles(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CheckRoles annotation = method.getAnnotation(CheckRoles.class);

        Object[] params = joinPoint.getArgs();
        AppUser appUser = (AppUser) params[1];

        if (annotation.mode == CheckRoles.Mode.ALL) {
            if (!appUserService.hasAllRoles(appUser, annotation.value())) {
                throw new UnauthorizedException();
            }
        } else if (annotation.mode == CheckRoles.Mode.ANY) {
            if (!appUserService.hasAnyRoles(appUser, annotation.value())) {
                throw new UnauthorizedException();
            }
        }

        try {
            return joinPoint.proceed(params);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage());
            throw throwable;
        }
    }
}
