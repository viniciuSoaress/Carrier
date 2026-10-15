package collection.address.entity;

import collection.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "address")
@Table(name = "address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String city;
    private String cep;
    private String number;

    public Address(String cep, String city, String number, User user) {
        this.cep = cep;
        this.city = city;
        this.number = number;
        this.user = user;
    }

    @JoinColumn(name = "users_id")
    @ManyToOne
    private User user;
}
