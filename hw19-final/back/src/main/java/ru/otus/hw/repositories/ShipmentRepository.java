package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.entities.Shipment;

import java.util.UUID;

public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {

}