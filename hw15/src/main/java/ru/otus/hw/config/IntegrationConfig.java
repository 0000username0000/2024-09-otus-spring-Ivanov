package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;
import ru.otus.hw.model.Item;
import ru.otus.hw.model.Order;
import ru.otus.hw.service.OrderProcessingService;

import java.util.Objects;

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
    public IntegrationFlow processedOrderFlow() {
        return IntegrationFlow.from(processedOrderChannel())
                .bridge()
                .get();
    }


    //Обрабатывается заказ с несколькими товарами и применяется скидка.
    @Bean
    public IntegrationFlow orderFlow(OrderProcessingService orderProcessingService) {
        return IntegrationFlow.from(orderInputChannel())
                .enrichHeaders(header -> header
                        .headerFunction("orderId", m -> ((Order) m.getPayload()).getId())
                )
                .split(Order.class, Order::getItems)
                .channel(itemProcessingChannel())
                .transform(Item.class, orderProcessingService::processItem)
                .aggregate(aggregator -> aggregator
                        .correlationStrategy(m -> m.getHeaders().get("orderId"))
                        .releaseStrategy(group -> Objects.equals(group.size(), group.getSequenceSize()))
                        .outputProcessor(group -> group.getMessages())
                )
                .handle(orderProcessingService, "finalizeOrder")
                .channel(processedOrderChannel())
                .get();
    }
}
