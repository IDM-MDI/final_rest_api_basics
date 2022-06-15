package com.epam.esm.config;

import com.epam.esm.security.jwt.JwtEntryPoint;
import com.epam.esm.security.jwt.JwtTokenFilter;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.security.oauth.CustomOAuth2UserService;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String USER_ENDPOINT = "/orders";
    private static final String REGISTRATION_ENDPOINT = "/users";
    private static final String LOGIN_ENDPOINT = "/login";
    private final JwtTokenProvider provider;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    public SecurityConfig(JwtTokenProvider provider, CustomOAuth2UserService customOAuth2UserService) {
        this.provider = provider;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtEntryPoint entryPoint() {
        return new JwtEntryPoint();
    }

    @Bean
    public JwtTokenFilter filter() {
        return new JwtTokenFilter(provider);
    }

    @Bean
    public OAuth2LoginSuccessHandler successHandler() {
        return new OAuth2LoginSuccessHandler();
    }
    @Bean
    public OAuth2LoginFailHandler failHandler() {
        return new OAuth2LoginFailHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
//                .exceptionHandling().authenticationEntryPoint(entryPoint())
//                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                .anyRequest().permitAll()
//                .and()
//                .oauth2Login(Customizer.withDefaults()).oauth2Login()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(HttpMethod.POST,REGISTRATION_ENDPOINT).permitAll()
                .antMatchers(HttpMethod.GET).authenticated()
                .antMatchers(USER_ENDPOINT).authenticated()
                .anyRequest().hasRole("ADMIN")
                .and()
                .oauth2Login()
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService)
                    .and()
                    .successHandler(successHandler())
                    .failureHandler(failHandler())
                .and()
                .addFilterBefore(filter(), UsernamePasswordAuthenticationFilter.class);
    }
}
