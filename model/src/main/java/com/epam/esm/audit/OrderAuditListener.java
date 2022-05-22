package com.epam.esm.audit;

import com.epam.esm.entity.Order;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class OrderAuditListener {
    @PreUpdate
    @PrePersist
    public void setLastUpdate(Order order) {
        order.setPrice(order.getGift().getPrice());
        order.setPurchaseTime(new Date());
    }
}
