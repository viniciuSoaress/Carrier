package dto;

import java.util.UUID;

public record AddressDTO(UUID address_id, String address_city, String address_cep, String address_number) {
}
