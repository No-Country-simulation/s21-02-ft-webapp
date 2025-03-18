package com.wallex.financial_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinancialPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancialPlatformApplication.class, args);
	}

}
