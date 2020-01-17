package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.TokenDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.InvalidTokenException;
import com.example.webservices.library.exceptions.TokenException;
import com.example.webservices.library.exceptions.TokenQuantityException;

import java.util.List;
import java.util.UUID;

public interface ITokenManager {
    List<UUID> RequestTokens(UUID customer, int quantity) throws TokenQuantityException, EntryNotFoundException;
    List<TokenDto> GetTokens(UUID customer) throws EntryNotFoundException;
    void UseToken(UUID tokenId) throws TokenException;


    UUID RequestToken(UUID customerId) throws EntryNotFoundException, TokenQuantityException;

    TokenDto GetToken(UUID tokenId) throws InvalidTokenException;

    void retireAll(UUID accountId);
}
