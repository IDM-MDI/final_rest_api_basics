package com.epam.esm.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tag")
@Getter @Setter @ToString
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private boolean deleted;
}
