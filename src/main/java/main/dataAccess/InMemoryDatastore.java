package main.dataAccess;

import main.dataAccess.IAccountDatastore;
import main.exceptions.DuplicateEntryException;
import main.dataAccess.ITokenDatastore;
import main.dataAccess.ITransactionDatastore;
import main.exceptions.EntryNotFoundException;
import main.exceptions.InvalidTokenException;
import main.models.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InMemoryDatastore implements IAccountDatastore, ITokenDatastore, ITransactionDatastore {
    private List<Account> accounts = new ArrayList<>();
    private List<Token> tokens = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    @Override
    public Customer getCustomer(UUID customerId) throws EntryNotFoundException {
        return (Customer)accounts.stream().filter(a -> a instanceof Customer && a.getAccountId().equals(customerId)).findFirst().orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public Merchant getMerchant(UUID merchantId) throws EntryNotFoundException {
        return (Merchant) accounts.stream().filter(a -> a instanceof Merchant && a.getAccountId().equals(merchantId)).findFirst().orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public Account getAccount(UUID accountId) throws EntryNotFoundException {
        return accounts.stream().filter(a -> a.getAccountId().equals(accountId)).findFirst().orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public <T extends Account> T addAccount(T account) throws DuplicateEntryException {
        if(accounts.stream().anyMatch(a -> a.getAccountId().equals(account.getAccountId())
                || (account instanceof Customer && a instanceof Customer) && ((Customer) a).getCpr().equals(((Customer) account).getCpr()))){
            throw new DuplicateEntryException();
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

    @Override
    public List<Transaction> getTransactions(Account account) {
        return this.transactions.stream().filter(t -> t.getCreditor().getAccountId().equals(account.getAccountId()) ||
                t.getDebtor().getAccountId().equals(account.getAccountId())
        ).collect(Collectors.toList());

    }
}
