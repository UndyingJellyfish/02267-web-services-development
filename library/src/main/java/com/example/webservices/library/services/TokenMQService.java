package com.example.webservices.library.services;

import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.RequestTokenDto;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.TokenDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.InvalidTokenException;
import com.example.webservices.library.exceptions.TokenException;
import com.example.webservices.library.exceptions.TokenQuantityException;
import com.example.webservices.library.interfaces.ITokenManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class TokenMQService extends RabbitMQBaseClass implements ITokenManager {

    public TokenMQService(RabbitTemplate rabbitTemplate, @Qualifier("tokens") DirectExchange exchange) {
        super(rabbitTemplate, exchange);
    }

    /**
     * @author s164424
     * @param customer which customer to give tokens
     * @param quantity amount of tokens to give
     * @return List of tokenId
     */
    @Override
    public List<UUID> RequestTokens(UUID customer, int quantity) {
        RequestTokenDto dto = new RequestTokenDto();
        dto.setCustomerId(customer);
        dto.setAmount(quantity);
        ResponseObject response = send(QUEUE_TOKENS_REQUEST, dto);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(), fromJson(response.getBody(), String.class));
        }
        return Arrays.asList(fromJson(response.getBody(), UUID[].class));
    }

    /**
     * @author s164424
     * @param customerId which account to search for
     * @return List of TokenDto
     * @throws EntryNotFoundException
     */
    @Override
    public List<TokenDto> GetTokens(UUID customerId) throws EntryNotFoundException {
        ResponseObject response = send(QUEUE_TOKENS_GET, customerId);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(), fromJson(response.getBody(), String.class));
        }
        return Arrays.asList(fromJson(response.getBody(), TokenDto[].class));
    }

    /**
     * @author s164424
     * @param tokenId id of the token to use
     * @throws TokenException
     */
    @Override
    public void UseToken(UUID tokenId) throws TokenException {
        ResponseObject response = send(QUEUE_TOKEN_USE, tokenId);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(), fromJson(response.getBody(), String.class));
        }
    }

    /**
     * @author s164424
     * @param customerId id of the customer to search for
     * @return tokenId
     * @throws EntryNotFoundException
     * @throws TokenQuantityException
     */
    @Override
    public UUID RequestToken(UUID customerId) throws EntryNotFoundException, TokenQuantityException {
        ResponseObject response = send(QUEUE_TOKEN_REQUEST, customerId);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(),fromJson(response.getBody(), String.class));
        }
        return fromJson(response.getBody(), UUID.class);
    }

    /**
     * @author s164424
     * @param tokenId id to search for
     * @return
     */
    @Override
    public TokenDto GetToken(UUID tokenId) {
        ResponseObject response = send(QUEUE_TOKEN_GET, tokenId);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(), fromJson(response.getBody(), String.class));
        }
        return fromJson(response.getBody(),TokenDto.class);
    }

    /**
     * @author s164434
     * @param accountId account for whom to retire all tokens
     */
    @Override
    public void retireAll(UUID accountId) {
        ResponseObject response = send(QUEUE_TOKENS_RETIRE, accountId);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(), fromJson(response.getBody(), String.class));
        }
    }

    /**
     * @author s164434
     * @param customerId
     * @return
     */
    @Override
    public int GetActiveTokens(UUID customerId) {
        ResponseObject response = send(QUEUE_TOKENS_ACTIVE, customerId);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(), fromJson(response.getBody(), String.class));
        }
        return fromJson(response.getBody(), int.class);
    }
}
