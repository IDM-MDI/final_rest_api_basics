package com.epam.esm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tag")
@Getter @Setter
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

//    @ManyToMany(mappedBy = "tagList")
//    private List<GiftCertificate> giftCertificates;

    @Column(name = "deleted")
    private boolean deleted;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "deleted = " + deleted + ")";
    }
}
