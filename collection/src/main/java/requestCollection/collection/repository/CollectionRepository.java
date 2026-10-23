package requestCollection.collection.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import requestCollection.collection.dto.CollectionResponseDTO;
import requestCollection.collection.entity.Collection;

import java.util.UUID;

public interface CollectionRepository extends JpaRepository<Collection, UUID> {
    Page<Collection> findByUserId(UUID userId, Pageable pageable);
}
