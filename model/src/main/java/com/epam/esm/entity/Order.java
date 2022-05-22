package com.epam.esm.entity;

import com.epam.esm.audit.OrderAuditListener;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@EntityListeners(OrderAuditListener.class)
@Table(name = "user_order")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "purchase_time")
//    @CreatedDate
    private Date purchaseTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumn(name = "gift_id")
//    @CreatedBy
    private GiftCertificate gift;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumn(name = "user_id")
//    @CreatedBy
    private User user;


    @Column(name = "deleted")
    private boolean deleted;

}
