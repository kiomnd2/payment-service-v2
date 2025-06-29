package com.subprj.paymentv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class Paymentv2Application {

    public static void main(String[] args) {
        SpringApplication.run(Paymentv2Application.class, args);
    }


    @Bean
    public Function<Flux<Message<String>>, Mono<Void>> consume() {
        return messageFlux -> {
            System.out.println("messageFlux = " + messageFlux);
            return messageFlux.then();
        };
    }
}
