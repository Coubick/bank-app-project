package org.example.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.UserEventDTO;
import org.example.user_dao.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserEventProducer {
    private static final String CLIENT_TOPIC = "client-topic";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public UserEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendUserEvent(String key, User user, String eventType) {
        UserEventDTO dto = new UserEventDTO(
                user.getLogin(),
                user.getName(),
                user.getAge(),
                user.getGender(),
                user.getHairColor(),
                eventType,
                LocalDateTime.now()
        );

        try {
            String json = objectMapper.writeValueAsString(dto);
            kafkaTemplate.send(CLIENT_TOPIC, key, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to serialize user event", e);
        }
    }
}