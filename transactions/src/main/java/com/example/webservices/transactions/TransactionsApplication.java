package com.example.webservices.transactions;

import com.example.webservices.transactions.dataAccess.JpaTransactionDatastore;
import com.example.webservices.transactions.dataAccess.TransactionRepository;
import com.example.webservices.transactions.interfaces.ITransactionDatastore;
import com.google.gson.*;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.plugins.Docket;

import javax.sql.DataSource;
import java.lang.reflect.Type;

import static com.example.webservices.library.RabbitHelper.*;


@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
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

    @Bean
    public Docket myApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
    public static class SpringfoxJsonToGsonAdapter implements JsonSerializer<Json> {

        @Override
        public JsonElement serialize(Json json, Type type, JsonSerializationContext context) {
            final JsonParser parser = new JsonParser();
            return parser.parse(json.value());
        }
    }

    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter() {
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(gson());
        return converter;
    }
    private Gson gson() {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Json.class, new SpringfoxJsonToGsonAdapter());
        return builder.create();
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
