package com.epam.esm.security.jwt;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.JwtAuthenticationException;
import com.epam.esm.security.JwtUserDetailsService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private static final String secret = "secret";
    private static final long validity = 3600000;
    private final JwtUserDetailsService userDetailsService;

    @Autowired
    public JwtTokenProvider(JwtUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("roles", getRoleNames(user.getRoles()));

        Date now = new Date();
        Date dateValidity = new Date(now.getTime() + validity);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(dateValidity)
                .signWith(SignatureAlgorithm.ES256,secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails user = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(user,"",user.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer_")) {
            return bearerToken.substring(7,bearerToken.length());
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
    private List<String> getRoleNames(List<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .toList();
    }

}
