package ru.otus.hw.models.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Data
@Table(name = "shipment")
public class Shipment implements Serializable {

    @Serial
    private static final long serialVersionUID = 4812954656207097221L;

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String shipmentNumber;

    @Column(nullable = false)
    private LocalDateTime shipmentDate;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column
    private String supplierInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Product product;

    @PrePersist
    public void generateOrderNumber() {
        if (Objects.isNull(this.shipmentNumber)) {
            this.shipmentNumber = String.format("SHIP-%d-%03d",
                    System.currentTimeMillis(),
                    ThreadLocalRandom.current().nextInt(1000)
            );
        }
    }
}