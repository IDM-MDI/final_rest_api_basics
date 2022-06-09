package com.epam.esm.security.jwt;

import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.JwtAuthenticationException;
import com.epam.esm.security.JwtUserDetailsService;

import io.jsonwebtoken.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final JwtUserDetailsService userDetailsService;
    @Value("${app.jwt.secret}")
    private String secret;
    @Value("${app.jwt.expiration}")
    private int validity;

    @Autowired
    public JwtTokenProvider(JwtUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String createToken(UserDto user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("roles", getRoleNames(user.getRoles()));

        Date now = new Date();
        Date dateValidity = new Date(now.getTime() + validity);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(dateValidity)
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }


    public Authentication getAuthentication(String token) {
        UserDetails user = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
         try {
             Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
             return !claims.getBody().getExpiration().before(new Date());
         } catch (JwtException | IllegalArgumentException e) {
             throw new JwtAuthenticationException("JWT Token is expired");
         }
    }
    private List<String> getRoleNames(List<RoleDto> roles) {
        return roles.stream()
                .map(RoleDto::getName)
                .toList();
    }

}
