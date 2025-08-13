package org.example.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.account_dao.Account;
import org.example.dto.AccountEventDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountEventProducer {
    private static final String ACCOUNT_TOPIC = "account-topic";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public AccountEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendAccountEvent(String key, Account account, String eventType) {

        AccountEventDTO dto = new AccountEventDTO(
                account.getUserDefinedId(),
                account.getBalance(),
                account.getOwnerLogin(),
                eventType,
                LocalDateTime.now()
        );

        try {
            String json = objectMapper.writeValueAsString(dto);
            kafkaTemplate.send(ACCOUNT_TOPIC, key, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to serialize user event", e);
        }
    }
}