package com.ewallet.transaction;

import lombok.Data;

@Data
public class TransactionSummary {

    private Double currentBalance;
    private Double totalAdded;
    private Double totalSent;
    private Double totalReceived;
}