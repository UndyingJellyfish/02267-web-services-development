package com.example.webservices.application.tokens;

import com.example.webservices.application.exceptions.EntryNotFoundException;
import com.example.webservices.application.exceptions.TokenQuantityException;
import com.example.webservices.library.models.Token;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tokens")
public class TokenController {

    private final TokenManager tokenManager;


    public TokenController(TokenManager tokenManager){
        this.tokenManager = tokenManager;
    }



    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Token> requestTokens(@RequestBody RequestTokenDto dto) {
        try {
            return tokenManager.RequestTokens(dto.getCustomerId(),dto.getAmount() );
        } catch (EntryNotFoundException e ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }catch(TokenQuantityException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Token> getTokens(@RequestBody UUID customerId) {
            return tokenManager.GetTokens(customerId);
    }
}