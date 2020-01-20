package com.example.webservices.tokens.controller;

import com.example.webservices.library.RabbitHelper;
import com.example.webservices.library.dataTransferObjects.RequestTokenDto;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.TokenDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.InvalidTokenException;
import com.example.webservices.library.exceptions.TokenException;
import com.example.webservices.library.exceptions.TokenQuantityException;
import com.example.webservices.library.interfaces.ITokenManager;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    @RabbitListener(queues = QUEUE_TOKEN_USE)
    public ResponseObject useToken(UUID tokenId) throws JsonProcessingException {
        try {
            this.tokenManager.UseToken(tokenId);
            return success();
        }catch (JsonProcessingException | TokenException e) {
            return failure(e.getMessage());
        }
    }
    @RabbitListener(queues = QUEUE_TOKENS_GET)
    public ResponseObject getTokens(UUID accountId) throws JsonProcessingException {
        try {
            List<TokenDto> tokens = this.tokenManager.GetTokens(accountId);
            return success(tokens);
        }catch (EntryNotFoundException | JsonProcessingException e) {
            return failure(e.getMessage());
        }
    }
    @RabbitListener(queues = QUEUE_TOKEN_GET)
    public ResponseObject getToken(UUID accountId) throws JsonProcessingException {
        try {
            TokenDto tokens = this.tokenManager.GetToken(accountId);
            return success(tokens);
        }catch (InvalidTokenException | JsonProcessingException e) {
            return failure(e.getMessage());
        }
    }

    @RabbitListener(queues = QUEUE_TOKEN_REQUEST)
    public ResponseObject requestToken(RequestTokenDto requestTokenDto) throws JsonProcessingException {
        try {
            UUID token = this.tokenManager.RequestToken(requestTokenDto.getCustomerId());
            return success(token);
        } catch (EntryNotFoundException | TokenQuantityException | JsonProcessingException e) {
            return failure(e.getMessage());
        }
    }
    @RabbitListener(queues = QUEUE_TOKENS_REQUEST)
    public ResponseObject requestTokens(RequestTokenDto requestTokenDto) throws JsonProcessingException {
        try {
            List<UUID> tokens = this.tokenManager.RequestTokens(requestTokenDto.getCustomerId(), requestTokenDto.getAmount());
            return success(tokens);
        } catch (EntryNotFoundException | TokenQuantityException | JsonProcessingException e) {
            return failure(e.getMessage());
        }
    }
    @RabbitListener(queues = QUEUE_TOKENS_RETIRE)
    public ResponseObject retireTokens(UUID accountId) throws JsonProcessingException {
        try {
            this.tokenManager.retireAll(accountId);
            return success();
        }catch (Exception e){
            return failure(e.getMessage());
        }
    }
}
