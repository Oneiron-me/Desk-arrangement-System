package com.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan("com.oneiron")
public class OneIronApplication {

	private static final String application = "spring.config.location=classpath:/application.properties"
			+ ", classpath:/security.properties";
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(OneIronApplication.class)
			.properties(application)
			.run(args);
	}
	
}
