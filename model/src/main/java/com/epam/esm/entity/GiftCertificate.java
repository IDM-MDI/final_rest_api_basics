package com.epam.esm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "gift_certificate")
@Getter @Setter
@NoArgsConstructor
public class GiftCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "create_date",nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Column(name = "update_date")
    @LastModifiedDate
    private LocalDateTime updateDate;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "gift_tag",
            joinColumns = @JoinColumn(name = "gift_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tagList;

    @Column(name = "deleted")
    private boolean deleted;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "price = " + price + ", " +
                "duration = " + duration + ", " +
                "create_date = " + createDate + ", " +
                "update_date = " + updateDate + ", " +
                "deleted = " + deleted + ")";
    }
}
