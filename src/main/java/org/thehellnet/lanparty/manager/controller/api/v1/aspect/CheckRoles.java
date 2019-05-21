package org.thehellnet.lanparty.manager.controller.api.v1.aspect;

import org.thehellnet.lanparty.manager.model.constant.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRoles {

    Role[] value() default {};
}
