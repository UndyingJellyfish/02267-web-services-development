package com.example.webservices.application.dataAccess;

import com.example.webservices.application.exceptions.InvalidTokenException;
import com.example.webservices.library.models.Customer;
import com.example.webservices.library.models.Token;

import java.util.List;
import java.util.UUID;

public interface ITokenDatastore {
    List<Token> getTokens(Customer customer);
    List<Token> assignTokens(Customer customer, List<Token> tokens);
    Token getToken(UUID tokenId) throws InvalidTokenException;
}
