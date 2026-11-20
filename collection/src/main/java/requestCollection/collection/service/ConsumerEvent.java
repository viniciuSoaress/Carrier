package requestCollection.collection.service;

import dto.DeliverySchemaEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerEvent {

    private static final String TOPIC_NAME = "Carrier-module-to";
    private final CollectionService collectionService;

    public ConsumerEvent(CollectionService collectionService) {
        this.collectionService = collectionService;
    }


    @KafkaListener(topics = TOPIC_NAME, groupId = "consumer-02")
    public void listenerEvent(DeliverySchemaEvent event){
        this.collectionService.updateStatus(event.id(), event.status());
    }

}
