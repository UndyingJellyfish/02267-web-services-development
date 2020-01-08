package main;

import models.Account;
import models.Customer;
import models.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryDatastore implements IDatastore {
    private List<Account> accounts = new ArrayList<Account>();
    private List<Token> tokens = new ArrayList<Token>();

    @Override
    public Customer getCustomer(UUID customerId) {
        return null;
    }

    @Override
    public List<Token> getTokens(Customer customer) {
        return null;
    }

    @Override
    public List<Token> assignTokens(Customer customer, List<Token> tokens) {
        return null;
    }

    @Override
    public Token getToken(UUID tokenId) {
        return null;
    }
}
