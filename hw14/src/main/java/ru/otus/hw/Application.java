package ru.otus.hw;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.printf("Link: %n%s%n", "http://localhost:8080");
        try {
            Console.main(args);
        } catch (SQLException e) {
			System.err.println("Start application error: " + e.getMessage());
			e.printStackTrace();
        }
    }

}
