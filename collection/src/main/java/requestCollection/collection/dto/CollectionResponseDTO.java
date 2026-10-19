package requestCollection.collection.dto;

import requestCollection.address.dto.AddressResponseDTO;
import requestCollection.address.dto.UserSummaryDTO;
import requestCollection.collection.entity.Collection;
import requestCollection.collection.entity.CollectionStatus;

import java.util.UUID;

public record CollectionResponseDTO(UUID id, String item_name, Integer quantity, Double weight, CollectionStatus status, AddressResponseDTO address){

    public CollectionResponseDTO(Collection collection){
        this(
                collection.getId(),
                collection.getItem_name(),
                collection.getQuantity(),
                collection.getWeight(),
                collection.getStatus(),
                new AddressResponseDTO(
                        collection.getAddress().getId(),
                        collection.getAddress().getCity(),
                        collection.getAddress().getCep(),
                        collection.getAddress().getNumber(),
                        new UserSummaryDTO(
                                collection.getUser().getId(),
                                collection.getUser().getName(),
                                collection.getUser().getEmail()
                        )
                )
        );
    }
}
