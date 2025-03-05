package ru.otus.hw.service;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.Item;
import ru.otus.hw.model.ProcessedOrder;

import java.util.List;
import java.util.UUID;

@Service
public class OrderProcessingService {

    public Item processItem(Item item) {
        item.setPrice(item.getPrice() * 0.9);
        return item;
    }

    public ProcessedOrder finalizeOrder(Message<List<Item>> message) {
        List<Item> items = message.getPayload();
        Long orderId = (Long) message.getHeaders().get("orderId");
        return new ProcessedOrder(orderId, items, UUID.randomUUID().toString());
    }

    public void handleProcessedOrder(Message<List<Item>> message) {
        List<Item> items = message.getPayload();
    }
}
