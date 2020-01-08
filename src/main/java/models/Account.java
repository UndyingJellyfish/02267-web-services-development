package models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Account {

    private String name;
    private UUID accountId;

    private List<Transaction> transactions = new ArrayList<>();


    public Account(String name){
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Name must be not null");
        }
        this.name = name;
        this.accountId = UUID.randomUUID();
    }


    public List<Transaction> getTransactions(){
        return this.transactions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getAccountId() {
        return accountId;
    }

}
