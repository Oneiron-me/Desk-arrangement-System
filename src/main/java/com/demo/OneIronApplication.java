package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan("com.oneiron")
public class OneIronApplication {

	public static void main(String[] args) {
		SpringApplication.run(OneIronApplication.class, args);
	}
	
}
