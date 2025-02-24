package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import ru.otus.hw.model.Item;
import ru.otus.hw.model.Order;
import ru.otus.hw.service.OrderProcessingService;

import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannel orderInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel itemProcessingChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel processedOrderChannel() {
        return new DirectChannel();
    }


    @Bean
    public IntegrationFlow orderFlow(OrderProcessingService orderProcessingService) {
        return IntegrationFlow.from(orderInputChannel())
                .enrichHeaders(header -> header
                        .headerFunction("orderId", m -> ((Order) m.getPayload()).getId()) // Добавляем orderId в заголовки
                )
                .split(Order.class, Order::getItems) // Разделяем заказ на товары
                .channel(itemProcessingChannel())
                .transform(Item.class, orderProcessingService::processItem) // Обрабатываем товар
                .aggregate(aggregator -> aggregator
                        .correlationStrategy(m -> m.getHeaders().get("orderId")) // Используем orderId из заголовков для агрегации
                        .releaseStrategy(group -> Objects.equals(group.size(), group.getSequenceSize()))
                        .outputProcessor(group -> group.getMessages()) // Возвращаем коллекцию сообщений
                )
                .handle(orderProcessingService, "finalizeOrder") // Завершаем заказ
                .channel(processedOrderChannel())
                .get();
    }

    @Bean
    public IntegrationFlow processedOrderFlow() {
        return IntegrationFlow.from(processedOrderChannel())
                .bridge() // Просто передаём сообщение дальше
                .get();
    }

}
