package org.thehellnet.lanparty.manager.configuration.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.thehellnet.lanparty.manager.model.persistence.AppUser;
import org.thehellnet.lanparty.manager.model.persistence.Role;
import org.thehellnet.lanparty.manager.service.AppUserService;
import org.thehellnet.lanparty.manager.service.AppUserTokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHENTICATION_HEADER = "X-Auth-Token";

    private final AppUserService appUserService;
    private final AppUserTokenService appUserTokenService;

    public AuthenticationFilter(AppUserService appUserService, AppUserTokenService appUserTokenService) {
        this.appUserService = appUserService;
        this.appUserTokenService = appUserTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenString = request.getHeader(AUTHENTICATION_HEADER);
        if (tokenString != null) {
            AppUser appUser = appUserTokenService.getAppUserByToken(tokenString);
            if (appUser != null) {
                List<Role> roles = appUserService.getAppUserRoles(appUser);
                List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(appUser.getEmail(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
