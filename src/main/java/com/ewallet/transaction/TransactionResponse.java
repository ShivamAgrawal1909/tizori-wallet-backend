package com.ewallet.transaction;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private Long id;

    private Long fromUserId;
    private Long toUserId;

    private String senderName;
    private String senderEmail;

    private String receiverName;
    private String receiverEmail;

    private Double amount;

    private String type;
    private String status;

    private LocalDateTime createdAt;
}