package com.example.webservices.payments;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PaymentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentsApplication.class, args);
    }

    @Bean
    public BankService getBankService(){
        return new BankServiceService().getBankServicePort();
    }

    @Bean
    @Qualifier("accounts")
    public DirectExchange getAccountsExchange(){
        return new DirectExchange("accounts");
    }
    @Bean
    @Qualifier("tokens")
    public DirectExchange getTokensExchange(){
        return new DirectExchange("tokens");
    }

    @Bean
    @Qualifier("transactions")
    public DirectExchange getTransactionsExchange(){
        return new DirectExchange("transactions");
    }

}
