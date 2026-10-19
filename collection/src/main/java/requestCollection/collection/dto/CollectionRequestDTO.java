package requestCollection.collection.dto;

import requestCollection.collection.entity.CollectionStatus;

import java.util.UUID;

public record CollectionRequestDTO(String item_name, Integer quantity, Double weight, CollectionStatus status,
                                   UUID users_id, UUID address_id) {
}
