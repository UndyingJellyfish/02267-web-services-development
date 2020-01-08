package main;

import models.Customer;
import models.Token;

import java.util.List;
import java.util.UUID;

public interface IDatastore {
    public Customer getCustomer(UUID customerId);
    public List<Token> getTokens(Customer customer);
    public List<Token> assignTokens(Customer customer, List<Token> tokens);
    public Token getToken(UUID tokenId);
}
