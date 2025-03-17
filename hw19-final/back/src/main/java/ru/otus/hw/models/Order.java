package ru.otus.hw.models;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 4669281880796354184L;

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    // Статусы: CREATED, PROCESSING, COMPLETED, CANCELLED
    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems;
}
