package com.example.webservices.transactions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.webservices.library.*;


@SpringBootApplication
public class TransactionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionsApplication.class, args);
    }

    private void derp(){
        DummyClass d = new DummyClass();
    }
}
