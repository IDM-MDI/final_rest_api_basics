package com.epam.esm.security.jwt;

import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.entity.StatusName.ACTIVE;

public final class JwtUserFactory {

    private JwtUserFactory(){}

    public static JwtUser create(UserDto user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getStatus().getName().toUpperCase().equals(ACTIVE.name()),
                mapToGrantedAuthorities(user.getRoles()));
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<RoleDto> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName())));
        return authorities;
    }
}
