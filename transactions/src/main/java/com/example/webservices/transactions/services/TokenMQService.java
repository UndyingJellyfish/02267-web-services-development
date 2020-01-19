package com.example.webservices.transactions.services;

import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.RequestTokenDto;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.TokenDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.InvalidTokenException;
import com.example.webservices.library.exceptions.TokenException;
import com.example.webservices.library.exceptions.TokenQuantityException;
import com.example.webservices.library.interfaces.ITokenManager;
import gherkin.deps.com.google.gson.reflect.TypeToken;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class TokenMQService extends RabbitMQBaseClass implements ITokenManager {

    public TokenMQService(RabbitTemplate rabbitTemplate, @Qualifier("tokens") DirectExchange exchange) {
        super(rabbitTemplate, exchange);
    }

    @Override
    public List<UUID> RequestTokens(UUID customer, int quantity){
        RequestTokenDto dto = new RequestTokenDto();
        dto.setCustomerId(customer);
        dto.setAmount(quantity);
        ResponseObject response = send(QUEUE_TOKENS_REQUEST, dto);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new RuntimeException();
        }
        return Arrays.asList(fromJson(response.getBody(), UUID[].class));
    }

    @Override
    public List<TokenDto> GetTokens(UUID customerId) throws EntryNotFoundException {
        ResponseObject response = send(QUEUE_TOKENS_GET, customerId);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new RuntimeException();
        }
        return Arrays.asList(fromJson(response.getBody(), TokenDto[].class));
    }

    @Override
    public void UseToken(UUID tokenId) throws TokenException {

    }

    @Override
    public UUID RequestToken(UUID customerId) throws EntryNotFoundException, TokenQuantityException {
        return null;
    }

    @Override
    public TokenDto GetToken(UUID tokenId) throws InvalidTokenException {
        return null;
    }

    @Override
    public void retireAll(UUID accountId) {

    }
}
