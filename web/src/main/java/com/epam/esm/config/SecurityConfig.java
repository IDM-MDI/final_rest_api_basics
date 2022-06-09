package com.epam.esm.config;

import com.epam.esm.security.jwt.JwtEntryPoint;
import com.epam.esm.security.jwt.JwtTokenFilter;
import com.epam.esm.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Autowired
    public SecurityConfig(JwtTokenProvider provider) {
        this.provider = provider;
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(entryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()
//                .antMatchers(HttpMethod.GET).permitAll()
//                .antMatchers(LOGIN_ENDPOINT).permitAll()
//                .antMatchers(HttpMethod.POST,REGISTRATION_ENDPOINT).permitAll()
//                .antMatchers(USER_ENDPOINT).authenticated()
//                .anyRequest().hasRole("ADMIN")
                .and().addFilterBefore(filter(), UsernamePasswordAuthenticationFilter.class);
//                .apply(configurer);
    }
}
