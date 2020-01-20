package com.example.webservices.transactions;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.example.webservices.library.RabbitHelper.*;


@SpringBootApplication
public class TransactionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionsApplication.class, args);
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory){
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setReplyTimeout(Long.MAX_VALUE);
        return template;
    }
    @Bean
    public Queue queueAddTransaction(){
        return new Queue(QUEUE_TRANSACTION_ADD, true);
    }
    @Bean
    public Queue queueGetTransaction(){
        return new Queue(QUEUE_TRANSACTION_GET, true);
    }
    @Bean
    public Queue queueReporting(){
        return new Queue(QUEUE_REPORTING_HISTORY, true);
    }
    @Bean
    public Binding bindingGet(@Qualifier("transactions") DirectExchange exchange, Queue queueAddTransaction) {
        return BindingBuilder.bind(queueAddTransaction).to(exchange).with(queueAddTransaction.getName());
    }
    @Bean
    public Binding bindingAdd(@Qualifier("transactions") DirectExchange exchange, Queue queueGetTransaction) {
        return BindingBuilder.bind(queueGetTransaction).to(exchange).with(queueGetTransaction.getName());
    }
    @Bean
    public Binding bindingReporting(@Qualifier("reporting") DirectExchange exchange, Queue queueReporting) {
        return BindingBuilder.bind(queueReporting).to(exchange).with(queueReporting.getName());
    }


    @Bean
    @Qualifier("transactions")
    public DirectExchange getExchange(){
        return new DirectExchange("transactions");
    }
    @Bean
    @Qualifier("reporting")
    public DirectExchange getReportingExchange(){
        return new DirectExchange("reporting");
    }
    @Bean
    @Qualifier("tokens")
    public DirectExchange getTokensExchange(){
        return new DirectExchange("tokens");
    }
    @Bean
    @Qualifier("accounts")
    public DirectExchange getAccountsExchange(){
        return new DirectExchange("accounts");
    }

}
