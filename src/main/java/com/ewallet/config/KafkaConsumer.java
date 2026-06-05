package com.ewallet.config;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "wallet-transactions", groupId = "ewallet-group")
    public void consumeMessage(String message) {
        System.out.println("Kafka message received: " + message);
    }
}