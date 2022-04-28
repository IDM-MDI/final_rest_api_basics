package com.epam.esm.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class GiftTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long giftId;
    private long tagId;
    private boolean isDeleted;

    public GiftTag(){}
}
