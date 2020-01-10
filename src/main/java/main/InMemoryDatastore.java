package main;

import interfaces.IAccountDatastore;
import interfaces.ITokenDatastore;
import interfaces.ITransactionDatastore;
import exceptions.InvalidTokenException;
import models.*;
import Interfaces.IAccountDatastore;
import Interfaces.ITokenDatastore;
import exceptions.InvalidTokenException;
import models.Account;
import models.Customer;
import models.Merchant;
import models.Token;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryDatastore implements IAccountDatastore, ITokenDatastore, ITransactionDatastore {
    private List<Account> accounts = new ArrayList<>();
    private List<Token> tokens = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    @Override
    public Customer getCustomer(UUID customerId) {
        return (Customer)accounts.stream().filter(a -> a instanceof Customer && a.getAccountId().equals(customerId)).findFirst().orElse(null);
    }

    @Override
    public Merchant getMerchant(UUID merchantId) {
        return (Merchant) accounts.stream().filter(a -> a instanceof Merchant && a.getAccountId().equals(merchantId)).findFirst().orElse(null);
    }

    @Override
    public Account addAccount(Account account) {
        if(accounts.stream().anyMatch(a -> a.getAccountId().equals(account.getAccountId()))){
            throw new IllegalArgumentException("The value is already in the list.");
        }
        accounts.add(account);
        return account;
    }

    @Override
    public List<Token> getTokens(Customer customer){
        return this.tokens.stream().filter(t -> t.getCustomer().equals(customer)).collect(Collectors.toList());
    }

    @Override
    public List<Token> assignTokens(Customer customer, List<Token> tokens) {
        List<UUID> newIds = tokens.stream().map(Token::getTokenId).collect(Collectors.toList());
        List<UUID> existingIds = this.tokens.stream().map(Token::getTokenId).collect(Collectors.toList());

        if(hasSharedIds(newIds, existingIds)){
            throw new IllegalArgumentException("Token with identical id exists");
        }
        if(hasDuplicateIds(newIds)){
            throw new IllegalArgumentException("Supplied tokens contain duplicate id's");
        }
        this.tokens.addAll(tokens);
        return tokens;
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

    @Override
    public Transaction GetTransactionByTokenId(UUID tokenId) {
        return this.transactions.stream().filter(t -> t.getToken().getTokenId().equals(tokenId)).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public void AddTransaction(Transaction transaction) {
        if(transactions.stream().anyMatch(t -> t.getToken().getTokenId().equals(transaction.getToken().getTokenId()))){
            throw new IllegalArgumentException("The value is already in the list.");
        }
        this.transactions.add(transaction);
    }
}
