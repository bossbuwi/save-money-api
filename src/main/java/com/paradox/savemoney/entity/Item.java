package com.paradox.savemoney.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    private long id;
    @Column(name = "amount")
    private double amount;
    @Column(name = "description")
    private String description;
}
