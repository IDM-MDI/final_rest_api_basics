package com.epam.esm.builder.impl;

import com.epam.esm.builder.ModelBuilder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.User;

import java.math.BigDecimal;
import java.util.Date;

public class OrderBuilder implements ModelBuilder {

    private Long id;
    private BigDecimal price;
    private Date purchaseTime;
    private GiftCertificate gift;
    private User user;
    private Status status;


    public OrderBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public OrderBuilder setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public OrderBuilder setPurchaseTime(Date purchaseTime) {
        this.purchaseTime = purchaseTime;
        return this;
    }

    public OrderBuilder setGift(GiftCertificate gift) {
        this.gift = gift;
        return this;
    }

    public OrderBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public OrderBuilder setStatus(Status status) {
        this.status = status;
        return this;
    }

    @Override
    public Order build() {
        Order order = new Order(id,price,purchaseTime,gift,user,status);
        clear();
        return order;
    }

    @Override
    public void clear() {
        id = null;
        price = null;
        purchaseTime = null;
        gift = null;
        user = null;
        status = null;
    }
}
