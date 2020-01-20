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


    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UUID> requestTokens(@RequestBody RequestTokenDto dto) {
        try {
            return tokenManager.RequestTokens(dto.getCustomerId(),dto.getAmount());
        } catch (EntryNotFoundException e ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }catch(TokenQuantityException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        } catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public int getActiveTokenCount(@PathVariable UUID customerId) {
        try {
            return tokenManager.GetActiveTokens(customerId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
