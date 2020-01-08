package Models;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {

    private List<Transaction> transactions = new ArrayList<>();

    public List<Transaction> getTransactions(){
        return this.transactions;
    }

}
