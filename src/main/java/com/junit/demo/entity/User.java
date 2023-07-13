package com.junit.demo.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @author : TuBQ
 * @since : 7/3/2023, Mon
 */
@Setter
@Getter
@Entity
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "age", nullable = false)
    private Integer age;
}
