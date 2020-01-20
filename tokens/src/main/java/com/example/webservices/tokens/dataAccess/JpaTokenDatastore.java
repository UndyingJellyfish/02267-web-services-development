package com.example.webservices.tokens.dataAccess;

import com.example.webservices.library.exceptions.InvalidTokenException;
import com.example.webservices.tokens.interfaces.ITokenDatastore;
import com.example.webservices.tokens.models.Token;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JpaTokenDatastore implements ITokenDatastore {

    private final TokenRepository tokenRepository;

    public JpaTokenDatastore(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    @Override
    public List<Token> getTokens(UUID customerId) {
        return this.tokenRepository.findTokensByCustomer(customerId);
    }

    @Override
    public List<Token> generateAndAssignTokens(UUID customerId, int quantity) {
        List<Token> newTokens = new ArrayList<>();

        for(int i = 0; i < quantity; i++){
            Token token = new Token(customerId);
            newTokens.add(token);
        }
        this.tokenRepository.saveAll(newTokens);
        return newTokens;
    }

    @Override
    public Token getToken(UUID tokenId) throws InvalidTokenException {
        return tokenRepository.findById(tokenId).orElseThrow(InvalidTokenException::new);
    }
}
