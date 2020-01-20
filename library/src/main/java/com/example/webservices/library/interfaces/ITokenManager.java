package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.TokenDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.InvalidTokenException;
import com.example.webservices.library.exceptions.TokenException;
import com.example.webservices.library.exceptions.TokenQuantityException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ITokenManager {

    /**
     * @param customer which customer to give tokens
     * @param quantity amount of tokens to give
     * @return a list of the token id's given to the {@param customer}
     * @throws TokenQuantityException thrown when the customer cannot request anymore tokens
     * @throws EntryNotFoundException thrown when the customer does not exist
     */
    List<UUID> RequestTokens(UUID customer, int quantity) throws TokenQuantityException, EntryNotFoundException;

    /**
     * @param customerId which account to search for
     * @return list of all tokens owned by a customer
     * @throws EntryNotFoundException thrown when the customer does not exist
     */
    List<TokenDto> GetTokens(UUID customerId) throws EntryNotFoundException;

    /**
     * @param tokenId id of the token to use
     * @throws TokenException when the token cannot be used, either because it is already spent or it does not exist
     */
    void UseToken(UUID tokenId) throws TokenException;

    /**
     * @param customerId id of the customer to search for
     * @return a single new token given to the customer
     * @throws EntryNotFoundException thrown when the customer does not exist
     * @throws TokenQuantityException thrown when the customer cannot request anymore tokens
     */
    UUID RequestToken(UUID customerId) throws EntryNotFoundException, TokenQuantityException;

    /**
     * @param tokenId id to search for
     * @return gets the token with the given id
     * @throws InvalidTokenException thrown when the token does not exist
     */
    TokenDto GetToken(UUID tokenId) throws InvalidTokenException;

    /**
     * @param accountId account for whom to retire all tokens
     */
    void retireAll(UUID accountId);

    int GetActiveTokens(UUID customerId);
}
