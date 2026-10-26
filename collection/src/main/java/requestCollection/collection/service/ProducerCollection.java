package requestCollection.collection.service;

import dto.CollectionSchema;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProducerCollection {

    private final static String TOPIC_NAME = "Carrier-module.";
    private final KafkaTemplate<UUID, CollectionSchema> kafkaTemplate;

    public ProducerCollection(KafkaTemplate<UUID, CollectionSchema> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCollection(CollectionSchema schema){
        this.kafkaTemplate.send(TOPIC_NAME, schema.id(), schema);
    }
}
