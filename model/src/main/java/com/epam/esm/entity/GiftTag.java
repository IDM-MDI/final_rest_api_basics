package com.epam.esm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "gift_tag")
@Getter
@Setter
@NoArgsConstructor
public class GiftTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "gift_id")
    private GiftCertificate gift;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Column(name = "deleted")
    private boolean deleted;
}
