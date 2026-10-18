package requestCollection.address.dto;

import java.util.UUID;

public record AddressRequestDTO(String city, String cep, String number, UUID users_id) {
}
