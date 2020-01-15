package com.example.webservices.application.tokens;

import com.example.webservices.application.dataAccess.IAccountDatastore;
import com.example.webservices.application.dataAccess.ITokenDatastore;
import com.example.webservices.application.exceptions.*;
import com.example.webservices.library.models.Customer;
import com.example.webservices.library.models.Token;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TokenManager implements ITokenManager {

    private ITokenDatastore datastore;
    private IAccountDatastore accountDatastore;

    public TokenManager(ITokenDatastore datastore, IAccountDatastore accountDatastore){
        this.datastore = datastore;
        this.accountDatastore = accountDatastore;
    }

    public List<Token> RequestTokens(UUID customerId, int quantity) throws TokenQuantityException, EntryNotFoundException {

        Customer customer = accountDatastore.getCustomer(customerId);

        if(quantity > 5  || quantity < 1){
            throw new TokenQuantityException("Quantity must be [1,5]");
        }

        // Probably move check for any active tokens to other method.
        if(datastore.getTokens(customer.getAccountId()).stream().filter(t -> !t.isUsed()).count() > 1){
            throw new TokenQuantityException();
        }

        List<Token> tokens = new ArrayList<>();

        for(int i = 0; i < quantity; i++){
            Token token = new Token(customer);
            tokens.add(token);
        }

        return this.datastore.assignTokens(customer, tokens);
    }

    public List<Token> GetTokens(UUID customerId) {
        return this.datastore.getTokens(customerId);
    }

    public void UseToken(UUID tokenId) throws TokenException {
        Token token = this.datastore.getToken(tokenId);
        if(token.isUsed()){
            throw new UsedTokenException();
        }
        token.setUsed(true);
    }

    @Override
    public Token RequestToken(Customer customer) throws EntryNotFoundException, TokenQuantityException {
        return RequestTokens(customer.getAccountId(), 1).get(0);
    }

    @Override
    public Token GetToken(UUID tokenId) throws InvalidTokenException {
        return  this.datastore.getToken(tokenId);
    }

    @Override
    public void retireAll(UUID customerId) {
        this.GetTokens(customerId).forEach(t -> t.setUsed(true));
    }
}
