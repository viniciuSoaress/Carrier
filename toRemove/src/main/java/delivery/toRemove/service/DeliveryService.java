package delivery.toRemove.service;

import delivery.toRemove.entity.Delivery;
import delivery.toRemove.repository.DeliveryRepository;
import dto.CollectionSchema;
import dto.CollectionStatus;
import dto.DeliverySchemaEvent;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final ProducerEvent producerEvent;

    public DeliveryService(DeliveryRepository deliveryRepository, ProducerEvent producerEvent) {
        this.deliveryRepository = deliveryRepository;
        this.producerEvent = producerEvent;
    }

    public void creat(CollectionSchema dto){
        if (this.deliveryRepository.existsById(dto.id())) {
            System.out.println("Delivery com o ID " + dto.id() + " já existe. Ignorando criação.");
            return;
        }

        Delivery newDelivery = new Delivery(dto);
        this.deliveryRepository.save(newDelivery);

        System.out.println("Delivery salvo com sucesso no banco: " + dto.id());
    }

    @Transactional
    public Page<Delivery> getAll(int page, int size){
        int safePage = Math.max(page, 0);
        int safeSize = Math.clamp(size, 1, 100);
        Pageable pageable = PageRequest.of(
                safePage,
                safeSize,
                Sort.by(Sort.Direction.DESC, "usersName")
        );
        return this.deliveryRepository.findAll(pageable);
    }


    @Transactional
    public void updateStatus(UUID id, String status){
        Delivery delivery = this.deliveryRepository.findById(id)
                        .orElseThrow(() ->new RuntimeException("not exists!"));

        CollectionStatus newStatus;
        try {
            newStatus = CollectionStatus.valueOf(status.toUpperCase().trim());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }

        delivery.setStatus(newStatus);
        this.deliveryRepository.save(delivery);

        this.producerEvent.sendEvent(new DeliverySchemaEvent(id, newStatus.toString()));
    }

}
