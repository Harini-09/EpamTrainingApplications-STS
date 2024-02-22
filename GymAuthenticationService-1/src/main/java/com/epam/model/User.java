package com.epam.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String userName;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean isActive;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    LocalDate createDate = LocalDate.now();
}
