package main.interfaces;

import main.exceptions.InvalidTokenException;
import main.exceptions.TokenException;
import main.models.Customer;
import main.models.Token;

import java.util.List;
import java.util.UUID;

public interface ITokenManager {
    List<Token> RequestTokens(Customer customer, int quantity);
    List<Token> GetTokens(Customer customer);
    void UseToken(UUID tokenId) throws TokenException;


    Token RequestToken(Customer customer);

    Token GetToken(UUID tokenId) throws InvalidTokenException;
}
