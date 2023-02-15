package com.ute.shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.ute.common","com.ute.shopping"})
public class EWebShoppingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EWebShoppingApplication.class, args);
	}

}
