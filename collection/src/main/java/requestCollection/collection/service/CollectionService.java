package requestCollection.collection.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import requestCollection.address.entity.Address;
import requestCollection.address.repository.AddressRepository;
import requestCollection.collection.dto.CollectionRequestDTO;
import requestCollection.collection.dto.CollectionResponseDTO;
import requestCollection.collection.entity.Collection;
import requestCollection.collection.repository.CollectionRepository;
import requestCollection.user.entity.User;
import requestCollection.user.repository.UserRepository;

import java.util.UUID;

@Service
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public CollectionService(CollectionRepository collectionRepository, UserRepository userRepository, AddressRepository addressRepository) {
        this.collectionRepository = collectionRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    private User getUser(UUID userId){
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not folder."));
    }

    public void createCollection(CollectionRequestDTO data){

        Address address = this.addressRepository.findById(data.address_id())
                .orElseThrow(() -> new IllegalArgumentException("Address not exist."));
        User user = getUser(data.users_id());
        Collection newCollection = new Collection(data.item_name(), data.quantity(), data.weight(), data.status(), user, address);
        this.collectionRepository.save(newCollection);
    }

    public String deleteCollection(UUID id){
        Collection collection = this.collectionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not"));
        this.collectionRepository.delete(collection);
        return "Deletado";
    }

    public Page<CollectionResponseDTO> getAllCollection(UUID userId, int page, int size){
        User user = getUser(userId);
        Pageable pageable = PageRequest.of(page, size);
        Page<Collection> collectionPage = this.collectionRepository.findByUserId(user.getId(), pageable);

        return collectionPage.map(CollectionResponseDTO::new);
    }
}
