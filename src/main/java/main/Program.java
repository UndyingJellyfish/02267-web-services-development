package main;

import adaptors.RemoteBankAdaptor;
import interfaces.IBank;
import interfaces.ITokenDatastore;
import interfaces.ITokenManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Program {

    @Bean
    public IBank remoteBankAdaptor(){
        return new RemoteBankAdaptor();
    }

    @Bean
    public UserService userService(InMemoryDatastore datastore){
        return new UserService(datastore);
    }

    @Bean
    public ITokenDatastore tokenDatastore(){
        return new InMemoryDatastore();
    }

    @Bean
    public ITokenManager tokenManager(ITokenDatastore datastore){
        return new TokenManager(datastore);
    }
    @Bean
    public InMemoryDatastore datastore(){
        return new InMemoryDatastore();
    }

    @Bean
    public PaymentService paymentService(ITokenManager tokenManager, InMemoryDatastore datastore, IBank bank ){
        return new PaymentService(tokenManager,datastore ,datastore, bank);
    }

    public static void main(String[] args){
        SpringApplication.run(Program.class, args);
    }
}
