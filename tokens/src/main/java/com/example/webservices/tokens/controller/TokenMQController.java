package com.example.webservices.tokens.controller;

import com.example.webservices.library.RabbitHelper;
import com.example.webservices.library.dataTransferObjects.RequestTokenDto;
import com.example.webservices.library.dataTransferObjects.TokenDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.TokenQuantityException;
import com.example.webservices.library.interfaces.ITokenManager;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TokenMQController extends RabbitHelper {

    private final ITokenManager tokenManager;

    public TokenMQController(ITokenManager tokenManager){

        this.tokenManager = tokenManager;
    }

    @RabbitListener(queues = QUEUE_TOKENS_GET)
    public String getTokens(String jsonString){
        try {
            UUID accountId = fromJson(jsonString, UUID.class);
            List<TokenDto> tokens = this.tokenManager.GetTokens(accountId);
            return success(tokens);
        }catch (EntryNotFoundException e) {
            return failure(e.getMessage());
        }
    }
    
    @RabbitListener(queues = QUEUE_TOKENS_REQUEST)
    public String requestTokens(String jsonString){
        try {
            RequestTokenDto requestTokenDto = fromJson(jsonString, RequestTokenDto.class);
            List<UUID> tokens = this.tokenManager.RequestTokens(requestTokenDto.getCustomerId(), requestTokenDto.getAmount());
            return success(tokens);
        } catch (EntryNotFoundException | TokenQuantityException e) {
            return failure(e.getMessage());
        }
    }
}
