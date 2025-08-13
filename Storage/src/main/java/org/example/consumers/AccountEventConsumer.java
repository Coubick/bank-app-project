package org.example.consumers;

import lombok.RequiredArgsConstructor;
import org.example.services.Service;
import org.example.entities.AccountEventEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class AccountEventConsumer {

    private final Service service;

    @KafkaListener(topics = "account-topic", groupId = "storage-group")
    public void handleAccountEvent(String eventInfo, @Header(KafkaHeaders.RECEIVED_KEY) String eventKey) {
        AccountEventEntity entity = new AccountEventEntity();
        entity.setEventKey(eventKey);
        entity.setEventInfo(eventInfo);
        service.saveAccount(entity);
    }
}