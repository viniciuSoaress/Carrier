package delivery.toRemove.entity;

import dto.CollectionSchema;
import dto.CollectionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import jakarta.persistence.Column;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


@Entity
@Table(name = "delivery") // Garante que buscará a tabela minúscula 'delivery'
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {

    @Id
    private UUID id;

    @Column(name = "item_name")
    private String itemName;

    private Integer quantity;

    private Double weight;

    @Enumerated(EnumType.STRING) // IMPORTANTE: Salva o Enum como VARCHAR(20) no banco, e não como número
    private CollectionStatus status;

    @Column(name = "users_id")
    private UUID usersId;

    @Column(name = "users_name")
    private String usersName;

    @Column(name = "users_email")
    private String usersEmail;

    @Column(name = "address_id")
    private UUID addressId;

    @Column(name = "address_city")
    private String addressCity;

    @Column(name = "address_cep")
    private String addressCep;

    @Column(name = "address_number")
    private String addressNumber;

    public Delivery(CollectionSchema dto) {
        this.id = dto.id();
        this.itemName = dto.item_name();
        this.quantity = dto.quantity();
        this.weight = dto.weight();
        this.status = dto.status();
        this.usersId = dto.userDTO().users_id();
        this.usersName = dto.userDTO().users_name();
        this.usersEmail = dto.userDTO().users_email();
        this.addressId = dto.addressDTO().address_id();
        this.addressCity = dto.addressDTO().address_city();
        this.addressCep = dto.addressDTO().address_cep();
        this.addressNumber = dto.addressDTO().address_number();
    }
}

