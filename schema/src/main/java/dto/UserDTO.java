package dto;

import java.util.UUID;

public record UserDTO(UUID users_id, String users_name, String users_email) {
}
