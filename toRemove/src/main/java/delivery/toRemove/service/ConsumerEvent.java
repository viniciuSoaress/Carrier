package delivery.toRemove.service;

import dto.CollectionSchema;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerEvent {

    private static final String TOPIC_NAME = "Carrier-module.";
    private final DeliveryService deliveryService;

    public ConsumerEvent(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @KafkaListener(topics = TOPIC_NAME, groupId = "consumer_01")
    public void listener(CollectionSchema schema){
        this.deliveryService.creat(schema);
    }
}
