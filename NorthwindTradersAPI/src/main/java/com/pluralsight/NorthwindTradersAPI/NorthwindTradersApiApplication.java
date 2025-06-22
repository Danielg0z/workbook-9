package com.pluralsight.NorthwindTradersAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NorthwindTradersApiApplication {

	public static void main(String[] args) {

		if (args.length != 2) {
			System.out.println("Usage: java -jar app.jar <username> <password>");
			System.exit(1);
		}

		// Set system properties with the username and password so Spring can read them later.
		System.setProperty("dbUsername", args[0]);
		System.setProperty("dbPassword", args[1]);

		// This line starts the entire Spring Boot application.
		// It does 3 main things:
		// 1. Creates the Spring "ApplicationContext" (this is like the brain of Spring that manages everything).
		// 2. Scans for your @Component classes (like FilmApp, SimpleFilmDao, etc.) and creates them automatically.
		// 3. Starts the web server (if your app had web controllers), or calls CommandLineRunner beans (like your FilmApp).


		SpringApplication.run(NorthwindTradersApiApplication.class, args);
	}

}
