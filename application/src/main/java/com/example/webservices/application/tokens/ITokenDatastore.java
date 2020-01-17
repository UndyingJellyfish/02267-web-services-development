package com.example.webservices.application.tokens;

import com.example.webservices.library.exceptions.InvalidTokenException;

import java.util.List;
import java.util.UUID;

public interface ITokenDatastore {
    List<Token> getTokens(UUID customerId);
    void assignTokens(UUID customerId, List<Token> tokens);
    Token getToken(UUID tokenId) throws InvalidTokenException;
}
