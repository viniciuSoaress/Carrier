package dto;

import java.util.UUID;

public record CollectionSchema(
        UUID id,
        String item_name,
        Integer quantity,
        Double weight,
        CollectionStatus status,
        AddressDTO addressDTO,
        UserDTO userDTO) {}
