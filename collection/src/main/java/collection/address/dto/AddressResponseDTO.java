package collection.address.dto;

import collection.address.entity.Address;

import java.util.UUID;

public record AddressResponseDTO(UUID id, String city, String cep, String number, UserSummaryDTO user) {

    public AddressResponseDTO(Address address){
        this(
                address.getId(),
                address.getCity(),
                address.getCep(),
                address.getNumber(),
                new UserSummaryDTO(
                        address.getUser().getId(),
                        address.getUser().getName(),
                        address.getUser().getEmail()
                )
        );
    }
}
