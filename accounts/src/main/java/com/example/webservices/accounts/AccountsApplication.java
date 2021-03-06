package com.example.webservices.accounts;

import com.example.webservices.accounts.dataAccess.AccountRepository;
import com.example.webservices.accounts.dataAccess.JpaAccountDatastore;
import com.example.webservices.accounts.interfaces.IAccountDatastore;
import com.example.webservices.accounts.models.Account;
import com.example.webservices.accounts.models.Customer;
import com.example.webservices.accounts.models.Merchant;
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

import javax.sql.DataSource;

import static com.example.webservices.library.RabbitHelper.*;

/** @author s164407 */
@SpringBootApplication(scanBasePackages = "com.example.webservices")
public class AccountsApplication {

    @Bean
    public IAccountDatastore accountDatastore(AccountRepository<Account> accountRepository, AccountRepository<Customer> customerRepository, AccountRepository<Merchant> merchantRepository){
        return new JpaAccountDatastore(accountRepository, customerRepository, merchantRepository);
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
