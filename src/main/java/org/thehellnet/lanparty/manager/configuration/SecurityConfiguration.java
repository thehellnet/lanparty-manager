package org.thehellnet.lanparty.manager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.thehellnet.lanparty.manager.api.v1.ws.ShowcaseWSHandler;
import org.thehellnet.lanparty.manager.configuration.filter.AuthenticationFilter;
import org.thehellnet.lanparty.manager.configuration.mixin.TestAwareConfiguration;
import org.thehellnet.lanparty.manager.model.constant.RoleName;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements TestAwareConfiguration {

    private final AuthenticationFilter authenticationFilter;

    public SecurityConfiguration(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll();

        http.authorizeRequests()
                .antMatchers("/dev/**").permitAll()
                .antMatchers(ShowcaseWSHandler.URL + "/**").permitAll();

        http.authorizeRequests()
                .antMatchers("/api/public/v1/**").permitAll();

        http.authorizeRequests()
                .antMatchers("/api/public/rest/**").fullyAuthenticated()
                .antMatchers(HttpMethod.GET, "/api/public/rest/**").hasRole(RoleName.USER.name())
                .antMatchers(HttpMethod.POST, "/api/public/rest/**").hasRole(RoleName.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/api/public/rest/**").hasRole(RoleName.ADMIN.name())
                .antMatchers(HttpMethod.PATCH, "/api/public/rest/**").hasRole(RoleName.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/api/public/rest/**").hasRole(RoleName.ADMIN.name());

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/v2/api-docs")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/configuration/**")
                .antMatchers("/webjars/**")
                .antMatchers("/public");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> allowedOrigins = new ArrayList<>();
        allowedOrigins.add("*");

        List<String> allowedMethods = new ArrayList<>();
        allowedMethods.add(HttpMethod.GET.name());
        allowedMethods.add(HttpMethod.POST.name());
        allowedMethods.add(HttpMethod.PATCH.name());
        allowedMethods.add(HttpMethod.PUT.name());
        allowedMethods.add(HttpMethod.DELETE.name());

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(allowedOrigins);
        corsConfiguration.setAllowedMethods(allowedMethods);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
