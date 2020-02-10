package com.example.demo;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class SiChannelsApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext 
		= new SpringApplicationBuilder(SiChannelsApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> applicationContext.close()));
		
//		SpringApplication.run(SiChannelsApplication.class, args);

	}

}
