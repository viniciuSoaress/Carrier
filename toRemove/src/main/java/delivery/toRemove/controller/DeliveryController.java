package delivery.toRemove.controller;

import delivery.toRemove.entity.Delivery;
import delivery.toRemove.service.DeliveryService;
import dto.CollectionStatus;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    @PreAuthorize(("hasRole('CARRIER')"))
    public ResponseEntity<Page<Delivery>> getAll(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(this.deliveryService.getAll(page,size));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize(("hasRole('CARRIER')"))
    public ResponseEntity<String> updates(@PathVariable UUID id,@RequestBody @Valid DtoResponse data){
       this.deliveryService.updateStatus(id, data.status);
        return ResponseEntity.ok("update");
    }

    public record DtoResponse(String status){}
}
