package main;

import interfaces.IAccountDatastore;
import models.Account;
import models.Customer;
import models.Merchant;

public class UserService {
    private IAccountDatastore accountDatastore;

    public UserService(IAccountDatastore accountDatastore) {
        this.accountDatastore = accountDatastore;
    }

    private <T extends Account> T addAccount(T account){
        return this.accountDatastore.addAccount(account);
    }

    public Customer SignUpCustomer(String customerName) {
        Customer customer = new Customer(customerName);
        return addAccount(customer);
    }

    public Merchant SignUpMerchant(String merchantName) {
        Merchant merchant = new Merchant(merchantName);
        return addAccount(merchant);
    }
}
