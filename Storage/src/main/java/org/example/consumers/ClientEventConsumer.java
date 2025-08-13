package org.example.consumers;

import lombok.RequiredArgsConstructor;
import org.example.entities.ClientEventEntity;
import org.example.services.Service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ClientEventConsumer {

    private final Service service;

    @KafkaListener(topics = "client-topic", groupId = "storage-group")
    public void handleClientEvent(@Header(KafkaHeaders.RECEIVED_KEY) String eventKey, String eventInfo) {
        ClientEventEntity entity = new ClientEventEntity();
        entity.setEventKey(eventKey);
        entity.setEventInfo(eventInfo);

        service.saveClient(entity);
    }
}
