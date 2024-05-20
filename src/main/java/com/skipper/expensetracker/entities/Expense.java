package com.skipper.expensetracker.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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
    private User user;

    private Double amount;
    private String description;
    private Date date;

}
