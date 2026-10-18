package requestCollection.user.dto;

import requestCollection.user.entity.User;
import requestCollection.user.entity.UserRole;

public record RegisterDTO(String name, String password, String email, UserRole role) {

    public RegisterDTO(User user){
        this(user.getName(), user.getPassword(), user.getEmail(), user.getRole());
    }
}
