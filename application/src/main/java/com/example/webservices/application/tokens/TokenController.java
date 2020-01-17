package com.example.webservices.application.tokens;

import com.example.webservices.library.dataTransferObjects.RequestTokenDto;
import com.example.webservices.library.dataTransferObjects.TokenDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.TokenQuantityException;
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
    public List<UUID> requestTokens(@RequestBody RequestTokenDto dto) {
        try {
            return tokenManager.RequestTokens(dto.getCustomerId(),dto.getAmount());
        } catch (EntryNotFoundException e ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }catch(TokenQuantityException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TokenDto> getTokens(@PathVariable UUID customerId) {
            return tokenManager.GetTokens(customerId);
    }
}
