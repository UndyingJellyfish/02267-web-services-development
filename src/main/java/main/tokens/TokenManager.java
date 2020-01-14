package main.tokens;

import main.dataAccess.IAccountDatastore;
import main.dataAccess.ITokenDatastore;
import main.exceptions.EntryNotFoundException;
import main.exceptions.InvalidTokenException;
import main.exceptions.TokenException;
import main.exceptions.UsedTokenException;
import main.models.Customer;
import main.models.Token;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    public List<Token> RequestTokens(UUID customerId, int quantity) throws EntryNotFoundException {

        Customer customer = accountDatastore.getCustomer(customerId);

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

    public List<Token> GetTokens(UUID customerId) throws EntryNotFoundException {
        return this.datastore.getTokens(accountDatastore.getCustomer(customerId));
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
    public Token RequestToken(Customer customer) throws EntryNotFoundException {
        return RequestTokens(customer.getAccountId(), 1).get(0);
    }

    @Override
    public Token GetToken(UUID tokenId) throws InvalidTokenException {
        return  this.datastore.getToken(tokenId);
    }
}
