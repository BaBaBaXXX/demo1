package com.example.demo1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements BaseEntity <Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32, nullable = false, unique = true)
    private String login;

    @Column(length = 32, nullable = false)
    private String password;

    @Column(length = 64, nullable = false, unique = true)
    private String email;

    @Column(length = 32)
    private String telegram;

}
