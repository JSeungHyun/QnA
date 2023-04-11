package com.board.QnA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class QnAApplication {

	public static void main(String[] args) {
		SpringApplication.run(QnAApplication.class, args);
	}

}
