package com.ewallet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final String TOPIC = "wallet-transactions";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendWalletEvent(String message) {

        kafkaTemplate.send(TOPIC, message);

        System.out.println("Kafka Event Published: " + message);
    }
}