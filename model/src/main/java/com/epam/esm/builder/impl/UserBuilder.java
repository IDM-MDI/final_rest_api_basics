package com.epam.esm.builder.impl;

import com.epam.esm.builder.ModelBuilder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UserBuilder implements ModelBuilder {

    private long id;
    private String name;
    private List<GiftCertificate> orders;


    public UserBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setOrders(List<GiftCertificate> orders) {
        this.orders = orders;
        return this;
    }

    @Override
    public User build() {
        User result = new User();
        result.setId(id);
        result.setName(name);
        result.setOrders(orders);
        return result;
    }
}
