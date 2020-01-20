package com.example.webservices.tokens;

import com.example.webservices.tokens.dataAccess.JpaTokenDatastore;
import com.example.webservices.tokens.dataAccess.TokenRepository;
import com.example.webservices.tokens.interfaces.ITokenDatastore;

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
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import javax.sql.DataSource;
import java.lang.reflect.Type;

import static com.example.webservices.library.RabbitHelper.*;
@SpringBootApplication(scanBasePackages = "com.example.webservices")
public class TokensApplication {

    @Bean
    public ITokenDatastore tokenDatastore(TokenRepository tokenRepository){
        return new JpaTokenDatastore(tokenRepository);
    }
    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.sqlite.JDBC");
        dataSourceBuilder.url("jdbc:sqlite:token.db");
        return dataSourceBuilder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(TokensApplication.class, args);
    }

   /*@Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory){
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setReplyTimeout(Long.MAX_VALUE);
        return template;
    }*/

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


    @Bean
    public Queue queueUseToken(){
        return new Queue(QUEUE_TOKEN_USE, true);
    }
    @Bean
    public Queue queueRequestToken(){
        return new Queue(QUEUE_TOKEN_REQUEST, true);
    }
    @Bean
    public Queue queueGetToken(){
        return new Queue(QUEUE_TOKEN_GET, true);
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
    public Queue queueRetireTokens(){
        return new Queue(QUEUE_TOKENS_RETIRE, true);
    }
    @Bean
    public Binding bindingUseToken(@Qualifier("tokens") DirectExchange exchange, Queue queueUseToken) {
        return BindingBuilder.bind(queueUseToken).to(exchange).with(queueUseToken.getName());
    }
    @Bean
    public Binding bindingRequestToken(@Qualifier("tokens") DirectExchange exchange, Queue queueRequestToken) {
        return BindingBuilder.bind(queueRequestToken).to(exchange).with(queueRequestToken.getName());
    }
    @Bean
    public Binding bindingGetToken(@Qualifier("tokens") DirectExchange exchange, Queue queueGetToken) {
        return BindingBuilder.bind(queueGetToken).to(exchange).with(queueGetToken.getName());
    }
    @Bean
    public Binding bindingGetTokens(@Qualifier("tokens") DirectExchange exchange, Queue queueGetTokens) {
        return BindingBuilder.bind(queueGetTokens).to(exchange).with(queueGetTokens.getName());
    }
    @Bean
    public Binding bindingRequest(@Qualifier("tokens") DirectExchange exchange, Queue queueRequestTokens) {
        return BindingBuilder.bind(queueRequestTokens).to(exchange).with(queueRequestTokens.getName());
    }
    @Bean
    public Binding bindingRetire(@Qualifier("tokens") DirectExchange exchange, Queue queueRetireTokens) {
        return BindingBuilder.bind(queueRetireTokens).to(exchange).with(queueRetireTokens.getName());
    }
}
