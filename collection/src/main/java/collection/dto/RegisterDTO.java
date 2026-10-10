package collection.dto;

import collection.entity.User;
import collection.entity.UserRole;

public record RegisterDTO(String name, String password, String email, UserRole role) {

    public RegisterDTO(User user){
        this(user.getName(), user.getPassword(), user.getEmail(), user.getRole());
    }
}
