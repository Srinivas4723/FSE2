package com.blogsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BlogSiteApplication {
	/**
	 * Main Method
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(BlogSiteApplication.class, args);
	}

}
