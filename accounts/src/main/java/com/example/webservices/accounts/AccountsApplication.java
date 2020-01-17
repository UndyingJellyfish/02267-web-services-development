package com.example.webservices.accounts;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class AccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsApplication.class, args);
    }

    @Configuration
    public static class Config{

        @Bean
        @Qualifier("tokens")
        public DirectExchange getExchange(){
            return new DirectExchange("tokens");
        }
    }
}
