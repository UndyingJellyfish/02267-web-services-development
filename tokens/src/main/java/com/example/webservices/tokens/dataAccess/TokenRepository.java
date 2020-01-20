package com.example.webservices.tokens.dataAccess;

import com.example.webservices.tokens.models.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;
public interface TokenRepository extends CrudRepository<Token, UUID> {

    List<Token> findTokensByCustomer(UUID customerId);
}
