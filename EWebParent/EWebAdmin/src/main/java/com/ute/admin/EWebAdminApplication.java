package com.ute.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.ute.common","com.ute.admin"})
public class EWebAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(EWebAdminApplication.class, args);
	}

}
