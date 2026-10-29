package requestCollection.collection.service;

import dto.CollectionSchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import requestCollection.address.entity.Address;
import requestCollection.address.repository.AddressRepository;
import requestCollection.collection.dto.CollectionRequestDTO;
import requestCollection.collection.dto.CollectionResponseDTO;
import requestCollection.collection.entity.Collection;
import requestCollection.collection.mapper.CollectionMapper;
import requestCollection.collection.repository.CollectionRepository;
import requestCollection.user.entity.User;
import requestCollection.user.repository.UserRepository;

import java.util.UUID;

@Service
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProducerCollection producerCollection;

    public CollectionService(CollectionRepository collectionRepository, UserRepository userRepository, AddressRepository addressRepository, ProducerCollection producerCollection) {
        this.collectionRepository = collectionRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.producerCollection = producerCollection;
    }

    private User getUser(UUID userId){
        return this.userRepository.getReferenceById(userId);
    }

    public void createCollection(CollectionRequestDTO data){

        Address address = this.addressRepository.getReferenceById(data.address_id());
        User user = getUser(data.users_id());
        Collection newCollection = new Collection(
                data.item_name(),
                data.quantity(),
                data.weight(),
                data.status(),
                user,
                address
        );

        Collection collection = this.collectionRepository.save(newCollection);
        CollectionSchema collectionSchema = CollectionMapper.toDTO(collection);
        this.producerCollection.sendCollection(collectionSchema);
    }

    public String deleteCollection(UUID collectionId, UUID userId) {
        return collectionRepository.findById(collectionId)
                .map(collection -> {
                    if (collection.getUser().getId().equals(userId)) {
                        collectionRepository.delete(collection);
                        return "Deletado.";
                    }
                    return "Não autorizado.";
                })
                .orElse("Não encontrado.");
    }

    public Page<CollectionResponseDTO> getAllCollection(UUID userId, int page, int size){
        User user = getUser(userId);
        Pageable pageable = PageRequest.of(page, size);
        Page<Collection> collectionPage = this.collectionRepository.findByUserId(user.getId(), pageable);

        return collectionPage.map(CollectionResponseDTO::new);
    }
}
