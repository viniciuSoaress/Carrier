package domain;

import java.util.UUID;

public class ModelsDto {
    public record LoginDto(String email, String password){}
    public record RegisterDto(String email, String password, UserRole role, String name){}
    public record AuthenticationResponseDto(String token, UUID id){}

}
