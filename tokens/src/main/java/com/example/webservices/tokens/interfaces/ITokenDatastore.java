package com.example.webservices.tokens.interfaces;

import com.example.webservices.library.exceptions.InvalidTokenException;
import com.example.webservices.tokens.models.Token;

import java.util.List;
import java.util.UUID;

public interface ITokenDatastore {
    List<Token> getTokens(UUID customerId);
    List<Token> assignTokens(UUID customerId, int quantity);
    Token getToken(UUID tokenId) throws InvalidTokenException;
}
