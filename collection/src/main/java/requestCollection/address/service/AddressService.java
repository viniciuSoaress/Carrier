package requestCollection.address.service;

import requestCollection.address.dto.AddressRequestDTO;
import requestCollection.address.dto.AddressResponseDTO;
import requestCollection.address.entity.Address;
import requestCollection.address.repository.AddressRepository;
import requestCollection.user.entity.User;
import requestCollection.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    private User getUserById(UUID userId){
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não existente!"));
    }

    public void createdAddress(AddressRequestDTO data){
        Address address = new Address(data.cep(), data.city(), data.number(), getUserById(data.users_id()));
        this.addressRepository.save(address);
    }

    public Page<AddressResponseDTO> getAllAddress(int page, int size, UUID users_id){
        User user = getUserById(users_id);
        Pageable pageable = PageRequest.of(page, size);
        Page<Address> addresses = this.addressRepository.findByUserId(user.getId(), pageable);

        return addresses.map(AddressResponseDTO::new);
    }

    public AddressResponseDTO updateAddress(UUID id, AddressRequestDTO data){
        Address updateAddress = this.addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Emdereçõ não encontrado!"));

        updateAddress.setCep(data.cep());
        updateAddress.setUser(getUserById(data.users_id()));
        updateAddress.setCity(data.city());
        updateAddress.setNumber(data.number());

        this.addressRepository.save(updateAddress);
        return new AddressResponseDTO(updateAddress);
    }

    public void deleteAddress(UUID id){
        this.addressRepository.deleteById(id);
    }
}
