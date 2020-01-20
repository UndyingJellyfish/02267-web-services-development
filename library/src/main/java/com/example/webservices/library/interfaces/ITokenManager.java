package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.TokenDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.InvalidTokenException;
import com.example.webservices.library.exceptions.TokenException;
import com.example.webservices.library.exceptions.TokenQuantityException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.UUID;

public interface ITokenManager {
    List<UUID> RequestTokens(UUID customer, int quantity) throws TokenQuantityException, EntryNotFoundException, JsonProcessingException;
    List<TokenDto> GetTokens(UUID customer) throws EntryNotFoundException, JsonProcessingException;
    void UseToken(UUID tokenId) throws TokenException;


    UUID RequestToken(UUID customerId) throws EntryNotFoundException, TokenQuantityException, JsonProcessingException;

    TokenDto GetToken(UUID tokenId) throws InvalidTokenException, JsonProcessingException;

    void retireAll(UUID accountId);
}
