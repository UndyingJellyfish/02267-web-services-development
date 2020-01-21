package com.example.webservices.application.tokens;

import com.example.webservices.library.dataTransferObjects.RequestTokenDto;
import com.example.webservices.library.dataTransferObjects.TokenDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.TokenQuantityException;
import com.example.webservices.library.interfaces.ITokenManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tokens")
public class TokenController {

    private final ITokenManager tokenManager;

    public TokenController(ITokenManager tokenManager){
        this.tokenManager = tokenManager;
    }

    /**
     * @author s164424
     * @param dto {@inheritDoc}
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UUID> requestTokens(@RequestBody RequestTokenDto dto) {
        try {
            return tokenManager.RequestTokens(dto.getCustomerId(),dto.getAmount());
        } catch(ResponseStatusException e) {
            throw e;
        } catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    /**
     * @author s164407
     * @param accountId id of the account
     * @return the amount of active tokens the account has
     */
    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public int getActiveTokenCount(@PathVariable UUID accountId) {
        try {
            return tokenManager.GetActiveTokens(accountId);
        } catch (ResponseStatusException e){
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
