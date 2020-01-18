package com.example.webservices.accounts;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.webservices.library.RabbitHelper.*;

@SpringBootApplication
public class AccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsApplication.class, args);
    }
    @Bean
    @Qualifier("tokens")
    public DirectExchange getExchange(){
        return new DirectExchange("tokens");
    }

    @Bean
    public Queue queueGetAccount() {
        return new Queue(QUEUE_ACCOUNT_GET, true);
    }

    @Bean
    public Queue queueCreateCustomer() {
        return new Queue(QUEUE_CUSTOMER_SIGNUP, true);
    }
    @Bean
    public Queue queueCreateMerchant() {
        return new Queue(QUEUE_MERCHANT_SIGNUP, true);
    }

    @Bean
    @Qualifier("accounts")
    public DirectExchange exchange() {
        return new DirectExchange("accounts");
    }

    @Bean
    public Binding bindingGetAccount(@Qualifier("accounts") DirectExchange exchange, Queue queueGetAccount) {
        return BindingBuilder.bind(queueGetAccount).to(exchange).with(queueGetAccount.getName());
    }

    @Bean
    public Binding bindingCreateCustomer(@Qualifier("accounts") DirectExchange exchange, Queue queueCreateCutomer) {
        return BindingBuilder.bind(queueCreateCutomer).to(exchange).with(queueCreateCutomer.getName());
    }

    @Bean
    public Binding bindingCreateMerchant(@Qualifier("accounts") DirectExchange exchange, Queue queueCreateMerchant) {
        return BindingBuilder.bind(queueCreateMerchant).to(exchange).with(queueCreateMerchant.getName());
    }



}
