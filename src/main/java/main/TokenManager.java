package main;

import Interfaces.ITokenDatastore;
import Interfaces.ITokenManager;
import exceptions.InvalidTokenException;
import exceptions.TokenException;
import exceptions.UsedTokenException;
import models.Customer;
import models.Token;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TokenManager implements ITokenManager {

    private ITokenDatastore datastore;

    public TokenManager(ITokenDatastore datastore){
        this.datastore = datastore;
    }

    public List<Token> RequestTokens(Customer customer, int quantity) {

        if(quantity > 5  || quantity < 1){
            throw new IllegalArgumentException("Quantity must be [1,5]");
        }

        // Probably move check for any active tokens to other method.
        if(datastore.getTokens(customer).stream().filter(t -> !t.isUsed()).count() > 1){
            throw new IllegalArgumentException("Customer has active tokens");
        }

        List<Token> tokens = new ArrayList<>();

        for(int i = 0; i < quantity; i++){
            Token token = new Token(customer);
            tokens.add(token);
        }

        return this.datastore.assignTokens(customer, tokens);
    }

    public List<Token> GetTokens(Customer customer) {
        return this.datastore.getTokens(customer);
    }

    public void UseToken(UUID tokenId) throws TokenException {
        Token token = this.datastore.getToken(tokenId);
        if(token.isUsed()){
            throw new UsedTokenException();
        }
        token.setUsed(true);
        token.setUseDate(new Date());


    }

    @Override
    public Token RequestToken(Customer customer) {
        return RequestTokens(customer, 1).get(0);
    }

    @Override
    public Token GetToken(UUID tokenId) throws InvalidTokenException {
        return  this.datastore.getToken(tokenId);
    }
}
