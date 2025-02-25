package ru.otus.hw.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.model.Order;
import ru.otus.hw.model.ProcessedOrder;

@MessagingGateway
public interface OrderGateway {
    @Gateway(requestChannel = "orderInputChannel", replyChannel = "processedOrderChannel")
    ProcessedOrder processOrder(Order order);
}
