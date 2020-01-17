package com.example.webservices.application.tokens;
import com.example.webservices.library.exceptions.InvalidTokenException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InMemoryTokenDatastore implements ITokenDatastore {
    private List<Token> tokens = new ArrayList<>();

    public void flush(){
        tokens = new ArrayList<>();
    }



    @Override
    public List<Token> getTokens(UUID customerId){
        return this.tokens.stream().filter(t -> t.getCustomerId().equals(customerId)).collect(Collectors.toList());
    }

    @Override
    public void assignTokens(UUID customerId, List<Token> tokens) {
        List<UUID> newIds = tokens.stream().map(Token::getTokenId).collect(Collectors.toList());
        List<UUID> existingIds = this.tokens.stream().map(Token::getTokenId).collect(Collectors.toList());

        if(hasSharedIds(newIds, existingIds)){
            throw new IllegalArgumentException("Token with identical id exists");
        }
        if(hasDuplicateIds(newIds)){
            throw new IllegalArgumentException("Supplied tokens contain duplicate id's");
        }
        this.tokens.addAll(tokens);
    }

    @Override
    public Token getToken(UUID tokenId) throws InvalidTokenException {
        return this.tokens.stream().filter(t -> t.getTokenId().equals(tokenId)).findFirst().orElseThrow(InvalidTokenException::new);
    }




    // Extract me somewhere!
    private boolean hasSharedIds(List<UUID> first, List<UUID> second){
        return first.stream().anyMatch(second::contains);
    }

    // Extract me somewhere!
    private boolean hasDuplicateIds(List<UUID> list){
        Set<UUID> set = new HashSet<>();
        return list.stream().anyMatch(x -> !set.add(x));
    }


}
