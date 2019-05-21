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
import org.thehellnet.lanparty.manager.model.dto.JsonResponse;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.service.AppUserService;

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
    public Object checkRoles(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CheckRoles annotation = method.getAnnotation(CheckRoles.class);

        Object[] params = joinPoint.getArgs();
        AppUser appUser = (AppUser) params[0];

        if (!appUserService.hasRoles(appUser, annotation.value())) {
            return JsonResponse.getErrorInstance("User doesn't have permissions");
        }

        try {
            return joinPoint.proceed(params);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage());
            return JsonResponse.getErrorInstance(throwable.getMessage());
        }
    }
}
