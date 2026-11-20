package requestCollection.collection.service;

import dto.CollectionSchema;
import dto.CollectionStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
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

    @Transactional
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

    public void deleteCollection(UUID collectionId, UUID userId) {
        collectionRepository.findById(collectionId)
                .map(collection -> {
                    if (collection.getUser().getId().equals(userId)) {
                        collectionRepository.delete(collection);
                        return "Deletado.";
                    }
                    return "Não autorizado.";
                });
    }

    public Page<CollectionResponseDTO> getAllCollection(UUID userId, int page, int size){
        User user = getUser(userId);
        int safePage = Math.min(0, page);
        int safeSize = Math.clamp(size, 1, 100);
        Pageable pageable = PageRequest.of(
                safePage,
                safeSize,
                JpaSort.unsafe(Sort.Direction.DESC, "item_name")
        );
        Page<Collection> collectionPage = this.collectionRepository.findByUserId(user.getId(), pageable);

        return collectionPage.map(CollectionResponseDTO::new);
    }

    @Transactional
    public void updateStatus(UUID id, String status){
        Collection collection = this.collectionRepository.findById(id)
                .orElseThrow(() ->new RuntimeException("not exists!"));

        CollectionStatus newStatus;
        try {
            newStatus = CollectionStatus.valueOf(status.toUpperCase().trim());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }

        collection.setStatus(newStatus);

        this.collectionRepository.save(collection);
        System.out.println("status atualizado: " + newStatus);

    }
}
