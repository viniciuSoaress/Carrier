package collection.user.dto;

import collection.user.entity.User;
import collection.user.entity.UserRole;

public record RegisterDTO(String name, String password, String email, UserRole role) {

    public RegisterDTO(User user){
        this(user.getName(), user.getPassword(), user.getEmail(), user.getRole());
    }
}
