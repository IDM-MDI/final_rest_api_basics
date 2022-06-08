package com.epam.esm.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private List<Order> orders;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;
}
