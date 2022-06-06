package com.epam.esm.builder.impl;

import com.epam.esm.builder.ModelBuilder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UserBuilder implements ModelBuilder {

    private Long id;
    private String name;
    private List<Order> orders;

    private Status status;


    public UserBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setOrders(List<Order> orders) {
        this.orders = orders;
        return this;
    }

    public UserBuilder setStatus(Status status) {
        this.status = status;
        return this;
    }

    @Override
    public User build() {
        User result = new User();
        result.setId(id);
        result.setUsername(name);
        result.setOrders(orders);
        result.setStatus(status);
        return result;
    }
}
