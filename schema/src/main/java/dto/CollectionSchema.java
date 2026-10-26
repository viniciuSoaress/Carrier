package dto;

import requestCollection.collection.entity.Collection;
import java.util.UUID;

public record CollectionSchema(
        UUID id,
        String item_name,
        Integer quantity,
        Double weight,
        CollectionStatus status,
        AddressDTO addressDTO,
        UserDTO userDTO) {

    public static CollectionSchema fromEntity(Collection c) {
        return new CollectionSchema(
                c.getId(),
                c.getItem_name(),
                c.getQuantity(),
                c.getWeight(),
                c.getStatus(),
                new AddressDTO(
                        c.getAddress().getId(),
                        c.getAddress().getCity(),
                        c.getAddress().getCep(),
                        c.getAddress().getNumber()),
                new UserDTO(
                        c.getUser().getId(),
                        c.getUser().getName(),
                        c.getUser().getEmail())
        );
    }
}
