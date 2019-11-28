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
import org.thehellnet.lanparty.manager.configuration.filter.AuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthenticationFilter authenticationFilter;

    public SecurityConfiguration(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll();
        http.authorizeRequests().antMatchers("/api/public/login").permitAll();

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/public/rest/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/api/public/rest/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/public/rest/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/public/rest/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/public/rest/**").hasRole("ADMIN");

        http.authorizeRequests()
                .anyRequest().fullyAuthenticated();

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
}
