package requestCollection.collection.mapper;

import dto.AddressDTO;
import dto.CollectionSchema;
import dto.UserDTO;
import requestCollection.collection.entity.Collection;

public class CollectionMapper {

    public static CollectionSchema toDTO(Collection c){
        if(c == null) return null;

        return new CollectionSchema(
                c.getId(),
                c.getItem_name(),
                c.getQuantity(),
                c.getWeight(),
                c.getStatus(),
                new AddressDTO(
                        c.getAddress().getId(),
                        c.getAddress().getCity(),
                        c.getAddress().getCep(),
                        c.getAddress().getNumber()
                ),
                new UserDTO(
                        c.getUser().getId(),
                        c.getUser().getName(),
                        c.getUser().getEmail()
                )
        );
    }
}
