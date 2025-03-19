package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.entities.Order;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}
