package Interfaces;

import models.Customer;
import models.Token;

import java.util.List;
import java.util.UUID;

public interface ITokenManager {
    List<Token> RequestTokens(Customer customer, int quantity);
    List<Token> GetTokens(Customer customer);
    boolean UseToken(UUID tokenId);


}
