package Interfaces;

import models.Customer;
import models.Merchant;

public interface IBank {

    void transferMoney(Customer customer, Merchant merchant, double amount);
}
