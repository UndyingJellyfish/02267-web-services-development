package com.example.webservices.accounts;

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
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.plugins.Docket;

import java.lang.reflect.Type;

import javax.sql.DataSource;

import static com.example.webservices.library.RabbitHelper.*;

@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
public class AccountsApplication {

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
        SpringApplication.run(AccountsApplication.class, args);
    }

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.sqlite.JDBC");
        dataSourceBuilder.url("jdbc:sqlite:account.db");
        return dataSourceBuilder.build();
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
    public Queue queueChangeName() {
        return new Queue(QUEUE_ACCOUNT_CHANGENAME, true);
    }
    @Bean
    public Queue queueDelete() {
        return new Queue(QUEUE_ACCOUNT_DELETE, true);
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
    public Binding bindingGetAccount(@Qualifier("accounts") DirectExchange exchange, Queue queueGetAccount) {
        return BindingBuilder.bind(queueGetAccount).to(exchange).with(queueGetAccount.getName());
    }

    @Bean
    public Binding bindingCreateCustomer(@Qualifier("accounts") DirectExchange exchange, Queue queueCreateCustomer) {
        return BindingBuilder.bind(queueCreateCustomer).to(exchange).with(queueCreateCustomer.getName());
    }

    @Bean
    public Binding bindingCreateMerchant(@Qualifier("accounts") DirectExchange exchange, Queue queueCreateMerchant) {
        return BindingBuilder.bind(queueCreateMerchant).to(exchange).with(queueCreateMerchant.getName());
    }

    @Bean
    public Binding bindingChangeName(@Qualifier("accounts") DirectExchange exchange, Queue queueChangeName) {
        return BindingBuilder.bind(queueChangeName).to(exchange).with(queueChangeName.getName());
    }
    @Bean
    public Binding bindingDelete(@Qualifier("accounts") DirectExchange exchange, Queue queueDelete) {
        return BindingBuilder.bind(queueDelete).to(exchange).with(queueDelete.getName());
    }



}
