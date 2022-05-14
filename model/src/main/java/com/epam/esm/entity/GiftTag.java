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
    private long id;

    @Column(name = "deleted")
    private boolean deleted;
}
