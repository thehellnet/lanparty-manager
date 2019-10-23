package org.thehellnet.lanparty.manager.api.v1.controller.aspect

import org.springframework.aop.aspectj.annotation.AspectJProxyFactory
import org.springframework.mock.web.MockHttpServletRequest
import org.thehellnet.lanparty.manager.ContextSpecification
import org.thehellnet.lanparty.manager.api.v1.controller.AppUserController
import org.thehellnet.lanparty.manager.exception.controller.UnauthorizedException
import org.thehellnet.lanparty.manager.model.persistence.AppUser
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken
import org.thehellnet.lanparty.manager.service.AppUserService
import org.thehellnet.lanparty.manager.service.LoginService
import org.thehellnet.lanparty.manager.service.crud.AppUserCrudService

class TokenControllerAspectTest extends ContextSpecification {

    private MockHttpServletRequest request

    private AppUserService appUserService
    private AppUserCrudService appUserCrudService
    private LoginService loginService

    private AppUserController proxy

    def setup() {
        AppUserController target = new AppUserController(appUserCrudService, loginService)
        AspectJProxyFactory factory = new AspectJProxyFactory(target)

        TokenControllerAspect aspect = webApplicationContext.getBean(TokenControllerAspect)
        factory.addAspect(aspect)

        proxy = factory.getProxy()
        request = new MockHttpServletRequest()
    }

    def "checkToken for AppUserController.isTokenValid() without x-auth-token header"() {
        when:
        proxy.isTokenValid(request, null)

        then:
        thrown UnauthorizedException
    }

    def "checkToken for AppUserController.isTokenValid() with empty x-auth-token header"() {
        given:
        request.addHeader("x-auth-token", "")

        when:
        proxy.isTokenValid(request, null)

        then:
        thrown UnauthorizedException
    }

    def "checkToken for AppUserController.isTokenValid() with invalid x-auth-token header"() {
        given:
        request.addHeader("x-auth-token", APPUSERTOKEN)

        when:
        proxy.isTokenValid(request, null)

        then:
        thrown UnauthorizedException
    }

    def "checkToken for AppUserController.isTokenValid() with valid x-auth-token header"() {
        given:
        AppUser appUser = appUserRepository.findByEmail("admin")
        appUserTokenRepository.save(new AppUserToken(APPUSERTOKEN, appUser))

        request.addHeader("x-auth-token", APPUSERTOKEN)

        when:
        proxy.isTokenValid(request, null)

        then:
        noExceptionThrown()
    }
}
