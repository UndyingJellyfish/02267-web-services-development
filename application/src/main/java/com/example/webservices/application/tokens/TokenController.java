package com.example.webservices.application.tokens;

import com.example.webservices.application.exceptions.EntryNotFoundException;
import com.example.webservices.library.models.Token;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
        } catch (EntryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }
}
