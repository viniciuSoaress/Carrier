package requestCollection.user.mapper;

import domain.ModelsDto;
import requestCollection.user.entity.User;

public class MapperModelsDto {

    public static ModelsDto.RegisterDto toDto(User u){
        if(u == null) return null;

        return new ModelsDto.RegisterDto(
                u.getEmail(),
                u.getPassword(),
                u.getRole(),
                u.getName()
        );
    }
}
