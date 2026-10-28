package requestCollection.collection.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import requestCollection.collection.dto.CollectionRequestDTO;
import requestCollection.collection.dto.CollectionResponseDTO;
import requestCollection.collection.service.CollectionService;
import requestCollection.user.entity.User;

import java.util.UUID;


@RestController
@RequestMapping("/collection")
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CollectionRequestDTO data){
        this.collectionService.createCollection(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<CollectionResponseDTO>> getAll(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ){

        UUID userId = user.getId();
        return ResponseEntity.ok(this.collectionService.getAllCollection(userId, page, size));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user){
        UUID userId = user.getId();
        this.collectionService.deleteCollection(userId);
        return ResponseEntity.status(203).build();
    }
}
