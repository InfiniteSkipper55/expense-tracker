package com.skipper.expensetracker.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String username;

    private String password;
    private String email;
    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "user")
    private Set<Expense> expenses;

}
