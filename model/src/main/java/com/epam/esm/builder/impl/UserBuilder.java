package com.epam.esm.builder.impl;

import com.epam.esm.builder.ModelBuilder;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UserBuilder implements ModelBuilder {

    private Long id;
    private String name;
    private String password;
    private List<Order> orders;
    private List<Role> roles;

    private String status;


    public UserBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setOrders(List<Order> orders) {
        this.orders = orders;
        return this;
    }

    public UserBuilder setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    public UserBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public User build() {
        User result = new User();
        result.setId(id);
        result.setUsername(name);
        result.setPassword(password);
        result.setOrders(orders);
        result.setStatus(status);
        result.setRoles(roles);
        clear();
        return result;
    }

    @Override
    public void clear() {
        id = null;
        name = null;
        password = null;
        orders = null;
        status = null;
        roles = null;
    }
}
