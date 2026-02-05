package com.relatospapel.ms_books_payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.relatospapel.ms_books_payments.client")
public class MsBooksPaymentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsBooksPaymentsApplication.class, args);
	}

}
