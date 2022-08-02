package com.epam.esm.config;

import com.epam.esm.security.jwt.JwtTokenFilter;
import com.epam.esm.security.oauth.OAuth2LoginFailHandler;
import com.epam.esm.security.oauth.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String USER_ENDPOINT = "/order/**";
    private static final String REGISTRATION_ENDPOINT = "/users";
    private static final String LOGIN_ENDPOINT = "/login";
    private static final String OAUTH_ENDPOINT = "/login/oauth";
    private static final String ADMIN_ROLE = "ADMIN";
    private final OAuth2LoginSuccessHandler successHandler;
    private final OAuth2LoginFailHandler failHandler;
    private final JwtTokenFilter filter;

    @Autowired
    public SecurityConfig(OAuth2LoginSuccessHandler successHandler,
                          OAuth2LoginFailHandler failHandler,
                          JwtTokenFilter filter) {
        this.successHandler = successHandler;
        this.failHandler = failHandler;
        this.filter = filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
//                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .antMatchers(LOGIN_ENDPOINT).permitAll()
                    .antMatchers(HttpMethod.POST,REGISTRATION_ENDPOINT).permitAll()
                    .antMatchers(USER_ENDPOINT).authenticated()
                    .antMatchers(OAUTH_ENDPOINT).authenticated()
                    .antMatchers(HttpMethod.GET).permitAll()
                    .anyRequest()
                        .hasRole(ADMIN_ROLE)
                .and()
                .oauth2Login()
                    .successHandler(successHandler)
                    .failureHandler(failHandler)
                .and()
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
