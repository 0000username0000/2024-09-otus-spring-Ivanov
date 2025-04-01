package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(Application.class, args);
		System.out.printf("Link: %n%s%n", "http://localhost:3000");
		System.out.printf("Swagger: %n%s%n", "http://localhost:8080/swagger-ui");
		System.out.println("logins: anton, ivan");
		System.out.println("password: password");
	}
}
