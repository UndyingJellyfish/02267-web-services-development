package main.dataAccess;

import main.exceptions.DuplicateEntryException;
import main.models.Account;
import main.models.Customer;
import main.models.Merchant;

import java.util.UUID;

public interface IAccountDatastore {
    Customer getCustomer(UUID customerId);
    <T extends Account> T addAccount(T account) throws DuplicateEntryException;

    Merchant getMerchant(UUID merchantId);
}