package main;

import models.Account;
import models.Customer;
import models.Token;

import java.util.List;
import java.util.UUID;

public interface IAccountDatastore {
    public Customer getCustomer(UUID customerId);
    public Account addAccount(Account account);

}
