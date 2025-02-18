package ru.otus.hw;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication(exclude = de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration.class)
public class Application {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(Application.class, args);
		Console.main(args);
	}

}
