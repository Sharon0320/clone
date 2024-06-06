package com.gachon.ReAction_bank_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
public class ReActionBankServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReActionBankServerApplication.class, args);
	}
}