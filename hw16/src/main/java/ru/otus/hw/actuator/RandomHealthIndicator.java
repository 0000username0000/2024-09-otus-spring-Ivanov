package ru.otus.hw.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Objects;
import java.util.Random;

@Component
public class RandomHealthIndicator implements HealthIndicator {

    private final Random random = new Random();

    @Override
    public Health health() {
        boolean isHealthy = checkSomeCondition();
        if (isHealthy) {
            return Health.up().withDetail("Custom Check", "Четное время").build();
        } else {
            return Health.down().withDetail("Custom Check", "Нечетное время").build();
        }
    }

    private boolean checkSomeCondition() {
        int currentSecond = LocalTime.now().getSecond();
        return Objects.equals(currentSecond % 2, 0);
    }
}
