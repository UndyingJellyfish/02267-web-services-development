package com.example.webservices.payments;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
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

import static com.example.webservices.library.RabbitHelper.QUEUE_PAYMENT_REFUND;
import static com.example.webservices.library.RabbitHelper.QUEUE_PAYMENT_TRANSFER;

/** @author s164434*/

@SpringBootApplication(scanBasePackages = "com.example.webservices")
public class PaymentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentsApplication.class, args);
    }

    @Bean
    public BankService getBankService(){
        return new BankServiceService().getBankServicePort();
    }

    @Bean
    public Queue queueTransfer() {
        return new Queue(QUEUE_PAYMENT_TRANSFER, true);
    }
    @Bean
    public Queue queueRefund() {
        return new Queue(QUEUE_PAYMENT_REFUND, true);
    }
    @Bean
    @Qualifier("payments")
    public DirectExchange getPaymentsExchange(){
        return new DirectExchange("payments");
    }
    @Bean
    public Binding bindingTransfer(@Qualifier("payments") DirectExchange exchange, Queue queueTransfer) {
        return BindingBuilder.bind(queueTransfer).to(exchange).with(queueTransfer.getName());
    }
    @Bean
    public Binding bindingRefund(@Qualifier("payments") DirectExchange exchange, Queue queueRefund) {
        return BindingBuilder.bind(queueRefund).to(exchange).with(queueRefund.getName());
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
    @Qualifier("reporting")
    public DirectExchange getReportingExchange(){
        return new DirectExchange("reporting");
    }

    @Bean
    @Qualifier("transactions")
    public DirectExchange getTransactionsExchange(){
        return new DirectExchange("transactions");
    }

}
