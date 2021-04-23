package com.tim9.bolnica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BolnicaApplication {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext appContext = SpringApplication.run(BolnicaApplication.class, args);
	}

}
