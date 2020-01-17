package com.example.webservices.application.tokens;

import com.example.webservices.application.exceptions.EntryNotFoundException;
import com.example.webservices.application.exceptions.InvalidTokenException;
import com.example.webservices.application.exceptions.TokenException;
import com.example.webservices.application.exceptions.TokenQuantityException;
import com.example.webservices.application.models.Customer;
import com.example.webservices.application.models.Token;

import java.util.List;
import java.util.UUID;

public interface ITokenManager {
    List<UUID> RequestTokens(UUID customer, int quantity) throws TokenQuantityException, EntryNotFoundException;
    List<TokenDto> GetTokens(UUID customer) throws EntryNotFoundException;
    void UseToken(UUID tokenId) throws TokenException;


    UUID RequestToken(Customer customer) throws EntryNotFoundException, TokenQuantityException;

    Token GetToken(UUID tokenId) throws InvalidTokenException;

    void retireAll(UUID accountId);
}
