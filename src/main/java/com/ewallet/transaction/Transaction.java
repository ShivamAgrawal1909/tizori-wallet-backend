package com.ewallet.transaction;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fromUserId;
    private Long toUserId;
    private Double amount;

    private String type; // TRANSFER, ADD_MONEY
    private String status; // SUCCESS, FAILED

    private LocalDateTime createdAt = LocalDateTime.now();
}