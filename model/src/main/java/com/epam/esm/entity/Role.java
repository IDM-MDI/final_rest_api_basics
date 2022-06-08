package com.epam.esm.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "role")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;
}
