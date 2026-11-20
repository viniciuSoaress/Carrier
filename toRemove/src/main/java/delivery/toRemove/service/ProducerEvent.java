package delivery.toRemove.service;

import dto.DeliverySchemaEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerEvent {

    private final KafkaTemplate<String, DeliverySchemaEvent> kafkaTemplate;
    private static final String TOPIC_NAME = "Carrier-module-to";

    public ProducerEvent(KafkaTemplate<String, DeliverySchemaEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(DeliverySchemaEvent event){
        kafkaTemplate.send(TOPIC_NAME, event.id().toString(), event);
    }
}
