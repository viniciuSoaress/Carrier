package requestCollection.collection.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import requestCollection.address.entity.Address;
import requestCollection.user.entity.User;

import java.util.UUID;

@Entity(name = "collection")
@Table(name = "collection")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String item_name;
    private Integer quantity;
    private Double weight;
    private CollectionStatus status = CollectionStatus.PENDING;


    public Collection(String item_name, Integer quantity, Double weight, CollectionStatus status, User user, Address address){
        this.item_name = item_name;
        this.quantity = quantity;
        this.weight = weight;
        this.status = status;
        this.user = user;
        this.address = address;
    }

    @JoinColumn(name = "users_id")
    @ManyToOne
    private User user;

    @JoinColumn(name = "address_id")
    @ManyToOne
    private Address address;
}
