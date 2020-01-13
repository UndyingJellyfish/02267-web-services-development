package main.dataAccess;

import main.exceptions.InvalidTokenException;
import main.models.Customer;
import main.models.Token;

import java.util.List;
import java.util.UUID;

public interface ITokenDatastore {
    List<Token> getTokens(Customer customer);
    List<Token> assignTokens(Customer customer, List<Token> tokens);
    Token getToken(UUID tokenId) throws InvalidTokenException;
}
