package com.example.webservices.transactions;

import com.example.webservices.transactions.dataAccess.JpaTransactionDatastore;
import com.example.webservices.transactions.dataAccess.TransactionRepository;
import com.example.webservices.transactions.interfaces.ITransactionDatastore;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;


import javax.sql.DataSource;
import java.lang.reflect.Type;

import static com.example.webservices.library.RabbitHelper.*;


@SpringBootApplication(scanBasePackages = "com.example.webservices")
public class TransactionsApplication {

    @Bean
    public ITransactionDatastore transactionDatastore(TransactionRepository transactionRepository){
        return new JpaTransactionDatastore(transactionRepository);
    }

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.sqlite.JDBC");
        dataSourceBuilder.url("jdbc:sqlite:transaction.db");
        return dataSourceBuilder.build();
    }
    public static void main(String[] args) {
        SpringApplication.run(TransactionsApplication.class, args);
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
    public Queue queueReportingSince(){
    return new Queue(QUEUE_REPORTING_HISTORY_DATE, true);
    }

    @Bean
    public Queue queueRefund(){
        return new Queue(QUEUE_TRANSACTION_REFUND, true);
    }

    @Bean
    public Binding bindingRefund(@Qualifier("transactions") DirectExchange exchange, Queue queueRefund) {
        return BindingBuilder.bind(queueRefund).to(exchange).with(queueRefund.getName());
    }
    @Bean
    public Binding bindingHistorySince(@Qualifier("reporting") DirectExchange exchange, Queue queueReportingSince) {
        return BindingBuilder.bind(queueReportingSince).to(exchange).with(queueReportingSince.getName());
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
    @Qualifier("tokens")
    public DirectExchange tokensExchange() {
        return new DirectExchange("tokens");
    }
    @Bean
    @Qualifier("accounts")
    public DirectExchange exchange() {
        return new DirectExchange("accounts");
    }
    @Bean
    @Qualifier("payments")
    public DirectExchange paymentsExchange() {
        return new DirectExchange("payments");
    }
    @Bean
    @Qualifier("reporting")
    public DirectExchange reportingExchange() {
        return new DirectExchange("reporting");
    }
    @Bean
    @Qualifier("transactions")
    public DirectExchange transactionsExchange() {
        return new DirectExchange("transactions");
    }


}
