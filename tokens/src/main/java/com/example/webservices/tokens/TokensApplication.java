package com.example.webservices.tokens;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.example.webservices.library.RabbitHelper.*;

@SpringBootApplication
public class TokensApplication {

    public static void main(String[] args) {
        SpringApplication.run(TokensApplication.class, args);
    }

    @Bean
    @Qualifier("accounts")
    public DirectExchange accountExchange(){
        return new DirectExchange("accounts");
    }
    @Bean
    @Qualifier("tokens")
    public DirectExchange tokensExchange(){
        return new DirectExchange("tokens");
    }

    @Bean
    public Queue queueGetTokens(){
        return new Queue(QUEUE_TOKENS_GET, true);
    }
    @Bean
    public Queue queueRequestTokens(){
        return new Queue(QUEUE_TOKENS_REQUEST, true);
    }
    @Bean
    public Binding bindingGet(@Qualifier("tokens") DirectExchange exchange, Queue queueGetTokens) {
        return BindingBuilder.bind(queueGetTokens).to(exchange).with(queueGetTokens.getName());
    }
    @Bean
    public Binding bindingRequest(@Qualifier("tokens") DirectExchange exchange, Queue queueRequestTokens) {
        return BindingBuilder.bind(queueRequestTokens).to(exchange).with(queueRequestTokens.getName());
    }
}
