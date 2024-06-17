package com.subprj.paymentv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Paymentv2Application {

    public static void main(String[] args) {
        SpringApplication.run(Paymentv2Application.class, args);
    }

}
