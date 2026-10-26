package requestCollection.collection.dto;

import dto.CollectionStatus;

import java.util.UUID;

public record CollectionRequestDTO(String item_name, Integer quantity, Double weight, CollectionStatus status,
                                   UUID users_id, UUID address_id) {
}
