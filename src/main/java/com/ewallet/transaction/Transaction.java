package com.ewallet.transaction;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fromUserId;
    private Long toUserId;
    private Double amount;

    private String type; // "TRANSFER", "ADD_MONEY"
    private String status; // "SUCCESS", "FAILED"

    private LocalDateTime createdAt = LocalDateTime.now();
}