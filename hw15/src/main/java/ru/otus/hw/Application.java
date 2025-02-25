package ru.otus.hw;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.otus.hw.gateway.OrderGateway;
import ru.otus.hw.model.Item;
import ru.otus.hw.model.Order;
import ru.otus.hw.model.ProcessedOrder;


import java.util.Arrays;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner run(OrderGateway orderGateway) {
        return args -> {
            Order order = new Order(1L, Arrays.asList(
                    new Item("Item1", 1000),
                    new Item("Item2", 500)
            ));
            ProcessedOrder processedOrder = orderGateway.processOrder(order);
            System.out.println("Processed Order: " + processedOrder);
        };
    }
}
