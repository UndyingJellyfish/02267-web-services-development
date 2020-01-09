package main;

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
        if(datastore.getTokens(customer).stream().anyMatch(t -> !t.isUsed())){
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

    public boolean UseToken(UUID tokenId) {
        try {
            Token token = this.datastore.getToken(tokenId);
            token.setUsed(true);
            token.setUseDate(new Date());
        }catch(Exception e){
            throw new IllegalArgumentException();
        }

        return true;
    }
}
