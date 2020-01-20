package com.example.webservices.payments;

import com.google.gson.*;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.plugins.Docket;

import java.lang.reflect.Type;

import static com.example.webservices.library.RabbitHelper.*;

@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
public class PaymentsApplication {

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
    @Qualifier("transactions")
    public DirectExchange getTransactionsExchange(){
        return new DirectExchange("transactions");
    }

}
