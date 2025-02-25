package ru.otus.hw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedOrder {

    private Long orderId;

    private List<Item> processedItems;

    private String trackingNumber;

    @Override
    public String toString() {
        return "ProcessedOrder{" +
                "orderId=" + orderId +
                ", items=" + processedItems +
                '}';
    }
}
