package com.example.webservices.tokens.services;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.TokenDto;
import com.example.webservices.library.exceptions.*;
import com.example.webservices.library.interfaces.IAccountService;
import com.example.webservices.library.interfaces.ITokenManager;
import com.example.webservices.tokens.interfaces.ITokenDatastore;
import com.example.webservices.tokens.models.Token;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TokenManager implements ITokenManager {

    private ITokenDatastore datastore;
    private IAccountService accountService;

    public TokenManager(ITokenDatastore datastore, IAccountService accountService){
        this.datastore = datastore;
        this.accountService = accountService;
    }

    public List<UUID> RequestTokens(UUID customerId, int quantity) throws TokenQuantityException, EntryNotFoundException {
        AccountDto customer = accountService.getCustomer(customerId);

        if(quantity > 5  || quantity < 1){
            throw new TokenQuantityException("Quantity must be [1,5]");
        }

        // Probably move check for any active tokens to other method.
        if(datastore.getTokens(customer.getAccountId()).stream().filter(t -> !t.isUsed()).count() > 1){
            throw new TokenQuantityException();
        }

        return this.datastore.generateAndAssignTokens(customer.getAccountId(), quantity).stream().map(Token::getTokenId).collect(Collectors.toList());
    }

    public List<TokenDto> GetTokens(UUID customerId) {
        return this.datastore.getTokens(customerId).stream().map(t -> new TokenDto(t.getTokenId(), t.isUsed())).collect(Collectors.toList());
    }

    public void UseToken(UUID tokenId) throws TokenException {
        Token token = this.datastore.getToken(tokenId);
        if(token.isUsed()){
            throw new UsedTokenException();
        }
        this.datastore.useToken(token);
    }

    @Override
    public UUID RequestToken(UUID customerId) throws EntryNotFoundException, TokenQuantityException {
        return RequestTokens(customerId, 1).get(0);
    }

    @Override
    public TokenDto GetToken(UUID tokenId) throws InvalidTokenException {
        Token token = this.datastore.getToken(tokenId);
        return new TokenDto(token.getTokenId(), token.isUsed(), token.getCustomerId());
    }

    @Override
    public void retireAll(UUID customerId) {
        this.datastore.getTokens(customerId).forEach(t -> t.setUsed(true));
    }

    @Override
    public int GetActiveTokens(UUID customerId) {
        return (int) this.GetTokens(customerId).stream().filter(t -> !t.isUsed()).count();
    }
}
