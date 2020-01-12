package main;

import interfaces.IAccountDatastore;
import models.Customer;

public class UserService {
    private IAccountDatastore accountDatastore;

    public UserService(IAccountDatastore accountDatastore) {
        this.accountDatastore = accountDatastore;
    }

    public Customer SignUpCustomer(String customerName) {
        Customer customer = new Customer(customerName);
        accountDatastore.addAccount(customer);
        return customer;
    }
}
