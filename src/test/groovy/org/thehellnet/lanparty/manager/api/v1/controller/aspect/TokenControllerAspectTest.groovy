package org.thehellnet.lanparty.manager.api.v1.controller.aspect

import org.springframework.aop.aspectj.annotation.AspectJProxyFactory
import org.springframework.mock.web.MockHttpServletRequest
import org.thehellnet.lanparty.manager.ContextSpecification
import org.thehellnet.lanparty.manager.api.v1.controller.AppUserController
import org.thehellnet.lanparty.manager.exception.controller.UnauthorizedException
import org.thehellnet.lanparty.manager.model.persistence.AppUser
import org.thehellnet.lanparty.manager.model.persistence.AppUserToken

class TokenControllerAspectTest extends ContextSpecification {

    private AppUserController proxy
    private MockHttpServletRequest request

    def setup() {
        AppUserController target = new AppUserController()
        AspectJProxyFactory factory = new AspectJProxyFactory(target)

        TokenControllerAspect aspect = webApplicationContext.getBean(TokenControllerAspect)
        factory.addAspect(aspect)

        proxy = factory.getProxy()
        request = new MockHttpServletRequest()
    }

    def "checkToken without x-auth-token header"() {
        when:
        proxy.isTokenValid(request, null)

        then:
        thrown UnauthorizedException
    }

    def "checkToken with empty x-auth-token header"() {
        given:
        request.addHeader("x-auth-token", "")

        when:
        proxy.isTokenValid(request, null)

        then:
        thrown UnauthorizedException
    }

    def "checkToken with invalid x-auth-token header"() {
        given:
        request.addHeader("x-auth-token", APPUSERTOKEN)

        when:
        proxy.isTokenValid(request, null)

        then:
        thrown UnauthorizedException
    }

    def "checkToken with valid x-auth-token header"() {
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
