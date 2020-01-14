package main.tokens;

import main.exceptions.EntryNotFoundException;
import main.exceptions.InvalidTokenException;
import main.exceptions.TokenException;
import main.models.Customer;
import main.models.Token;

import java.util.List;
import java.util.UUID;

public interface ITokenManager {
    List<Token> RequestTokens(UUID customer, int quantity) throws EntryNotFoundException;
    List<Token> GetTokens(UUID customer) throws EntryNotFoundException;
    void UseToken(UUID tokenId) throws TokenException;


    Token RequestToken(Customer customer) throws EntryNotFoundException;

    Token GetToken(UUID tokenId) throws InvalidTokenException;
}
