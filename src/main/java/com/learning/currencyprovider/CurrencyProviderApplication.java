package com.learning.currencyprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class CurrencyProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyProviderApplication.class, args);
	}

}
