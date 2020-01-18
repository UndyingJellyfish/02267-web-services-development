package com.example.webservices.application.tokens;

import com.example.webservices.application.stepdefs.AbstractSteps;
import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.AccountType;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.dataTransferObjects.TokenDto;
import com.example.webservices.library.exceptions.*;
import com.example.webservices.library.interfaces.IAccountService;
import com.example.webservices.library.interfaces.ITokenManager;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TokenManagerTest extends AbstractSteps {

    private ITokenManager tokenManager;
    private ITokenDatastore tokenDatastore = mock(ITokenDatastore.class);
    private IAccountService accountService = mock(IAccountService.class);
    private AccountDto customerDto;

    public TokenManagerTest( ) {

    }

    @Before
    public void Setup() {
        UUID customerId = UUID.randomUUID();
        SignupDto dto = new SignupDto("Bob","123",customerId.toString());

        this.customerDto = new AccountDto(customerId,
                dto.getName(),
                dto.getBankAccountId(),
                dto.getIdentifier(),
                AccountType.CUSTOMER);
        try {
            when(accountService.addCustomer(dto)).thenReturn(customerDto);
            this.tokenManager = new TokenManager(tokenDatastore,accountService);
        } catch (DuplicateEntryException e) {
            fail();
        }
    }

    @Test
    public void RequestTokens(){
        UUID customerId = customerDto.getAccountId();
        List<TokenDto> beforeTokens;
        try {
            when(tokenDatastore.getTokens(customerId))
                    .thenReturn(new ArrayList<Token>(){});
            beforeTokens = tokenManager.GetTokens(customerId);

            when(accountService.getCustomer(customerId))
                    .thenReturn(customerDto);
            List<UUID> tokenIds = tokenManager.RequestTokens(customerId,2);
            List<Token> tokens = new ArrayList<Token>(){};
            for(UUID id : tokenIds) {
                tokens.add(new Token(id));
            }

            when(tokenDatastore.getTokens(customerId))
                    .thenReturn(tokens);
            List<TokenDto> afterTokens = tokenManager.GetTokens(customerId);

            assertEquals(0, beforeTokens.size());
            assertEquals(2, afterTokens.size());
        } catch (EntryNotFoundException | TokenQuantityException e) {
            fail();
        }
    }

    @Test
    public void RequestNoTokens(){
        UUID customerId = customerDto.getAccountId();
        List<TokenDto> beforeTokens;
        try {
            when(tokenDatastore.getTokens(customerId))
                    .thenReturn(new ArrayList<Token>(){});
            beforeTokens = tokenManager.GetTokens(customerId);
            try{
                when(accountService.getCustomer(customerId))
                        .thenReturn(customerDto);
                 tokenManager.RequestTokens(customerId,0);
            }catch(TokenQuantityException ignored){        }

            when(tokenDatastore.getTokens(customerId))
                    .thenReturn(new ArrayList<Token>(){});
            List<TokenDto> afterTokens = tokenManager.GetTokens(customerId);

            assertEquals(beforeTokens.size(), afterTokens.size());
        } catch (EntryNotFoundException e) {
            fail();
        }

    }

    @Test
    public void RequestTooManyTokens(){
        UUID customerId = customerDto.getAccountId();
        try{
            when(accountService.getCustomer(customerId))
                        .thenReturn(customerDto);
            tokenManager.RequestTokens(customerId,6);
        } catch (TokenQuantityException ignored){

        } catch (EntryNotFoundException e) {
            fail();
        }
    }

    @Test
    public void RequestTokensWhenOverLimit(){
        UUID customerId = customerDto.getAccountId();
        try {
            when(accountService.getCustomer(customerId))
                    .thenReturn(customerDto);
            List<UUID> tokenIds = tokenManager.RequestTokens(customerId,2); // Any qty over 1

            List<Token> tokens = new ArrayList<Token>(){};
            for(UUID id : tokenIds) {
                tokens.add(new Token(id));
            }
            when(tokenDatastore.getTokens(customerId))
                    .thenReturn(tokens);
            List<TokenDto> beforeTokens = tokenManager.GetTokens(customerId);
            try{
                tokenManager.RequestTokens(customerId,1);
                fail();
            }catch(TokenQuantityException ignored){        }
            List<TokenDto> afterTokens = tokenManager.GetTokens(customerId);
            assertEquals(beforeTokens.size(), afterTokens.size());
        } catch (EntryNotFoundException | TokenQuantityException e) {
            fail();
        }

    }

    @Test
    public void UseExistingToken() {
        UUID customerId = customerDto.getAccountId();
        try {
            when(accountService.getCustomer(customerId))
                    .thenReturn(customerDto);
            tokenManager.RequestTokens(customerId,1);

            List<Token> tokens = new ArrayList<Token>(){};
            tokens.add(new Token(UUID.randomUUID()));

            try{
                when(tokenDatastore.getToken(tokens.get(0).getTokenId()))
                        .thenReturn(tokens.get(0));
                tokenManager.UseToken(tokens.get(0).getTokenId());
            } catch (TokenException e){
                fail();
            }
            when(tokenDatastore.getTokens(customerId))
                    .thenReturn(tokens);
            List<TokenDto> afterTokens = tokenManager.GetTokens(customerId);
            assertEquals(1, afterTokens.size());
            boolean isUsed = afterTokens.get(0).isUsed();
            assertTrue(isUsed);
        } catch (EntryNotFoundException | TokenQuantityException e) {
            fail();
        }

    }

    @Test
    public void UseNotExistingToken() {
        UUID tokenId = UUID.randomUUID();
        try{
            when(tokenDatastore.getToken(tokenId))
                    .thenThrow(InvalidTokenException.class);
            tokenManager.UseToken(tokenId);
            fail();
        } catch (TokenException e){
            assertTrue(e instanceof  InvalidTokenException);
        }
    }


}
