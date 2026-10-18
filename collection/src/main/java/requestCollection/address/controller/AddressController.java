package requestCollection.address.controller;

import requestCollection.address.dto.AddressRequestDTO;
import requestCollection.address.dto.AddressResponseDTO;
import requestCollection.address.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid AddressRequestDTO data){
        this.addressService.createdAddress(data);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{users_id}")
    public ResponseEntity<Page<AddressResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @PathVariable UUID users_id
            ){
        return ResponseEntity.ok(this.addressService.getAllAddress(page, size, users_id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid AddressRequestDTO data ){
        return ResponseEntity.ok(
                this.addressService.updateAddress(id, data)

        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        this.addressService.deleteAddress(id);
        return ResponseEntity.ok(null);
    }
}
