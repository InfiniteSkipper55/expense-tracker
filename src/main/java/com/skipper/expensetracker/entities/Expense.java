package com.skipper.expensetracker.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @ManyToOne
    private Category category;

    @ManyToOne
    @JsonIgnore
    private User user;

    private Double amount;
    private String description;
    private Date date;

}
