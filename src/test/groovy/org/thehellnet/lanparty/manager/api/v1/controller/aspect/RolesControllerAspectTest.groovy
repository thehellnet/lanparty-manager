package org.thehellnet.lanparty.manager.api.v1.controller.aspect

import org.springframework.aop.aspectj.annotation.AspectJProxyFactory
import org.springframework.mock.web.MockHttpServletRequest
import org.thehellnet.lanparty.manager.ContextSpecification
import org.thehellnet.lanparty.manager.api.v1.controller.AppUserController
import org.thehellnet.lanparty.manager.exception.controller.UnauthorizedException
import org.thehellnet.lanparty.manager.model.constant.Role
import org.thehellnet.lanparty.manager.model.persistence.AppUser
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken
import org.thehellnet.lanparty.manager.service.LoginService
import org.thehellnet.utility.PasswordUtility

class RolesControllerAspectTest extends ContextSpecification {

    private MockHttpServletRequest request

    private LoginService loginService;

    private AppUserController proxy

    private AppUser appUser

    def setup() {
        AppUserController target = new AppUserController(loginService)
        AspectJProxyFactory factory = new AspectJProxyFactory(target)

        TokenControllerAspect tokenControllerAspect = webApplicationContext.getBean(TokenControllerAspect)
        factory.addAspect(tokenControllerAspect)

        RolesControllerAspect rolesControllerAspect = webApplicationContext.getBean(RolesControllerAspect)
        factory.addAspect(rolesControllerAspect)

        proxy = factory.getProxy()

        appUser = appUserRepository.save(new AppUser(APPUSER_EMAIL, PasswordUtility.hash(APPUSER_PASSWORD)))
        appUserTokenRepository.save(new AppUserToken(APPUSERTOKEN, appUser))

        request = new MockHttpServletRequest()
        request.addHeader("x-auth-token", APPUSERTOKEN)
    }

    def "checkRoles for AppUserController.isTokenValid() with new user and no roles"() {
        when:
        proxy.isTokenValid(request, null)

        then:
        thrown UnauthorizedException
    }

    def "checkRoles for AppUserController.isTokenValid() with new user and minimum role"() {
        given:
        AppUser appUser = appUserRepository.findByEmail(APPUSER_EMAIL)
        appUser.roles.add(Role.ACTION_LOGIN)
        appUserRepository.save(appUser)

        when:
        proxy.isTokenValid(request, null)

        then:
        noExceptionThrown()
    }
}
