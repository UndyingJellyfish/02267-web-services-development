package com.example.webservices.application.tokens;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.dataTransferObjects.TokenDto;
import com.example.webservices.library.exceptions.*;
import com.example.webservices.library.interfaces.IAccountService;
import com.example.webservices.library.interfaces.ITokenManager;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class TokenManagerTest {

    private ITokenManager tokenManager;
    private IAccountService accountService = mock(IAccountService.class);
    private UUID customerId;

    public TokenManagerTest(ITokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Before
    public void Setup() {
        SignupDto dto = new SignupDto("Bob","123", UUID.randomUUID().toString());

        try {
            AccountDto accDto = accountService.addCustomer(dto);
            customerId = accDto.getAccountId();
        } catch (DuplicateEntryException e) {
            fail();
        }
    }

    @Test
    public void RequestTokens(){

        List<TokenDto> beforeTokens = null;
        try {
            beforeTokens = tokenManager.GetTokens(customerId);
            tokenManager.RequestTokens(customerId,2);
            List<TokenDto> afterTokens = tokenManager.GetTokens(customerId);
            assertEquals(0, beforeTokens.size());
            assertEquals(2, afterTokens.size());
        } catch (EntryNotFoundException | TokenQuantityException e) {
            fail();
        }

    }

    @Test
    public void RequestNoTokens(){
        List<TokenDto> beforeTokens = null;
        try {
            beforeTokens = tokenManager.GetTokens(customerId);
            try{
                tokenManager.RequestTokens(customerId,0);
            }catch(TokenQuantityException ignored){        }
            List<TokenDto> afterTokens = tokenManager.GetTokens(customerId);
            assertEquals(beforeTokens.size(), afterTokens.size());
        } catch (EntryNotFoundException e) {
            fail();
        }

    }

    @Test
    public void RequestTooManyTokens(){
        List<TokenDto> beforeTokens = null;
        try {
            beforeTokens = tokenManager.GetTokens(customerId);
            try{
                tokenManager.RequestTokens(customerId,6);
            }catch(TokenQuantityException ignored){        }
            List<TokenDto> afterTokens = tokenManager.GetTokens(customerId);
            assertEquals(beforeTokens.size(), afterTokens.size());
        } catch (EntryNotFoundException e) {
            fail();
        }

    }

    @Test
    public void RequestTokensWhenOverLimit(){
        try {
            tokenManager.RequestTokens(customerId,2); // Any qty over 1
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
        List<UUID> beforeTokens = null;
        try {
            beforeTokens = tokenManager.RequestTokens(customerId, 1);
            try{
                tokenManager.UseToken(beforeTokens.get(0));
            } catch (TokenException e){
                fail();
            }
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
        UUID id = UUID.randomUUID();
        try{
            tokenManager.UseToken(id);
            fail();
        } catch (TokenException e){
            assertTrue(e instanceof  InvalidTokenException);
        }
    }


}
