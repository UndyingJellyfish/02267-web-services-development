package com.example.webservices.tokens;

import com.example.webservices.tokens.dataAccess.JpaTokenDatastore;
import com.example.webservices.tokens.dataAccess.TokenRepository;
import com.example.webservices.tokens.interfaces.ITokenDatastore;
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

import javax.sql.DataSource;
import java.lang.reflect.Type;

import static com.example.webservices.library.RabbitHelper.*;
@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
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
    public Queue queueRetireTokens(){
        return new Queue(QUEUE_TOKENS_RETIRE, true);
    }
    @Bean
    public Binding bindingGet(@Qualifier("tokens") DirectExchange exchange, Queue queueGetTokens) {
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
