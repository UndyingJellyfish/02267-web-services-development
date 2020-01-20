package com.example.webservices.application;

import com.google.gson.Gson;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = "com.example.webservices")
@ConditionalOnClass(Gson.class)
@ConditionalOnMissingClass(value = "com.fasterxml.jackson.core.JsonGenerator")
@ConditionalOnBean(Gson.class)
public class Application {



    @Bean
    @ConditionalOnMissingBean
    public GsonHttpMessageConverter gsonHttpMessageConverter(Gson gson) {
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(gson);
        return converter;
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory){
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setReplyTimeout(Long.MAX_VALUE);
        return template;
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
    @Bean
    @Qualifier("reporting")
    public DirectExchange getReportingExchange(){
        return new DirectExchange("reporting");
    }
    @Bean
    @Qualifier("payments")
    public DirectExchange getPaymentsExchange(){
        return new DirectExchange("payments");
    }

}
