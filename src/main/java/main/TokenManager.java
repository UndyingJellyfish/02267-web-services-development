package main;

import models.Customer;
import models.Token;

import java.util.List;
import java.util.UUID;

public class TokenManager implements ITokenManager {

    public List<Token> RequestTokens(Customer customer, int quantity) {
        return null;
    }

    public Token RequestToken(Customer customer) {
        return null;
    }

    public List<Token> GetTokens(Customer customer) {
        return null;
    }

    public boolean UseToken(UUID tokenId) {
        return false;
    }
}
