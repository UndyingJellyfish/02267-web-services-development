package com.example.webservices.application.tokens;

import com.example.webservices.application.exceptions.EntryNotFoundException;
import com.example.webservices.application.exceptions.InvalidTokenException;
import com.example.webservices.application.exceptions.TokenException;
import com.example.webservices.library.models.Customer;
import com.example.webservices.library.models.Token;

import java.util.List;
import java.util.UUID;

public interface ITokenManager {
    List<Token> RequestTokens(UUID customer, int quantity) throws EntryNotFoundException;
    List<Token> GetTokens(UUID customer) throws EntryNotFoundException;
    void UseToken(UUID tokenId) throws TokenException;


    Token RequestToken(Customer customer) throws EntryNotFoundException;

    Token GetToken(UUID tokenId) throws InvalidTokenException;

    void retireAll(UUID accountId);
}
